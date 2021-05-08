package com.tahir.airmeetask.activities

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tahir.airmeetask.R
import com.tahir.airmeetask.UIHelper
import com.tahir.airmeetask.adapters.AppartmentsAdapter
import com.tahir.airmeetask.helpers.DatePickerHelper
import com.tahir.airmeetask.helpers.Utils
import com.tahir.airmeetask.interfaces.RVClickCallback
import com.tahir.airmeetask.models.Appartments
import com.tahir.airmeetask.viewstate.DataState
import com.tahir.airmeetask.viewstate.SubmitStatus
import com.tahir.airmeetask.vm.MainActivityViewModel
import com.tahir.airmeetask.vm.MyViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.appt_booking.*
import kotlinx.android.synthetic.main.appt_booking.view.*
import java.util.*


class MainActivity : AppCompatActivity(), RVClickCallback, View.OnClickListener {
    lateinit var mainactVm: MainActivityViewModel
    lateinit var appartmentAdapter: AppartmentsAdapter
    var appartments: List<Appartments> = emptyList()
    var book_start_date: MutableLiveData<String> = MutableLiveData<String>()
    var book_end_date: MutableLiveData<String> = MutableLiveData<String>()
    var search_start_date: MutableLiveData<String> = MutableLiveData<String>()
    var search_end_date: MutableLiveData<String> = MutableLiveData<String>()
    lateinit var datePicker: DatePickerHelper
    private val RECORD_REQUEST_CODE = 101
    var locationManager: LocationManager? = null
    var GpsStatus = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    fun init() {
        // initial configrations.
        setupPermissions()
        et_beds.setOnClickListener(this)
        btn_search.setOnClickListener(this)
        et_fromdate.setOnClickListener(this)
        et_todate.setOnClickListener(this)
        btn_filter.setOnClickListener(this)


    }

    /**
     * Subscribe to DataState Live data
     */
    private fun subscribeObservers() {
        mainactVm.dataState.observe(this, { dataState ->
// observing data state changes

            when (dataState) {


                is DataState.Success<List<Appartments>> -> {
                    appartments = dataState.data
                    appartmentAdapter.loadItems(appartments, this)
                    appartmentAdapter.notifyDataSetChanged()
                    displayProgressBar(false)

                }
                is DataState.Error -> {
                    displayProgressBar(false)
                    displayError(dataState.exception)
                }
                is DataState.Loading -> {

                    displayProgressBar(true)
                }
            }
        })


        search_start_date.observe(this, {
            et_fromdate.setText(it)

        })
        search_end_date.observe(this, {
            et_todate.setText(it)

        })
    }

    /*
      setupApptList will be used to setup appartment list
       */
    fun setupApptList(appartments: List<Appartments>) {

        rv_appartments.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        // Create a DividerItemDecoration whose orientation is vertical
        val vItemDecoration = DividerItemDecoration(
            this,
            DividerItemDecoration.VERTICAL
        )
        // Set the drawable on it
        vItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider)!!)
        // add decoration for list
        rv_appartments.addItemDecoration(
            vItemDecoration
        )
        // setting up recyclerview and also binding activity with the view-model
        appartmentAdapter = AppartmentsAdapter(this, appartments, this)
        rv_appartments?.adapter = appartmentAdapter


    }

    /**
     * This method will be used to show the error messages if any
     */
    private fun displayError(message: String?) {

        var msg: String?
        msg = message
        if (message == null) {

            msg = "unknown error"
        }
        UIHelper.showShortToastInCenter(this, msg!!)


    }


    /**
     * Display progressbar
     */
    private fun displayProgressBar(isDisplayed: Boolean) {
        progress_bar.visibility = if (isDisplayed) View.VISIBLE else View.GONE
    }

    override fun onItemClick(pos: Int) {
        dateChooser(pos)

    }


    fun dateChooser(pos: Int) {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        val viewGroup = findViewById<ViewGroup>(android.R.id.content)

        //then we will inflate the custom alert dialog xml that we created
        val dialogView: View =
            LayoutInflater.from(this).inflate(R.layout.appt_booking, viewGroup, false)


        //Now we need an AlertDialog.Builder object
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView)
        dialogView.message_box_content.text = "Do you want to Book '${appartments.get(pos).name}'"
        //finally creating the alert dialog and displaying it
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
        dialogView.et_start_date.setOnClickListener {

            showDatePickerDialog("book_start")
        }
        dialogView.et_end_date.setOnClickListener {

            showDatePickerDialog("book_end")
        }
        book_start_date.observe(this, {
            dialogView.et_start_date.setText(it)

        })
        book_end_date.observe(this, {
            dialogView.et_end_date.setText(it)

        })



        dialogView.btn_book.setOnClickListener {
            if (dialogView.et_start_date.text.toString().length == 0 || dialogView.et_end_date.text.toString().length == 0) {
                UIHelper.showShortToastInCenter(this, "please enter booking dates.")
                return@setOnClickListener

            }
            mainactVm.setStateEvent(
                SubmitStatus.Book,
                Appartments(
                    appartments.get(pos).id,
                    appartments.get(pos).bedrooms,
                    appartments.get(pos).name,
                    appartments.get(pos).latitude,
                    appartments.get(pos).longitude,
                    true,
                    Utils.stringToDate(dialogView.et_start_date.text.toString()),
                    Utils.stringToDate(dialogView.et_end_date.text.toString()),
                    appartments.get(pos).distance

                )
            )
            dialogView.et_start_date.setText("")
            dialogView.et_end_date.setText("")

            alertDialog.dismiss()
        }


    }

    private fun showDatePickerDialog(type: String) {

        val cal = Calendar.getInstance()
        val d = cal.get(Calendar.DAY_OF_MONTH)
        val m = cal.get(Calendar.MONTH)
        val y = cal.get(Calendar.YEAR)
        datePicker.showDialog(d, m, y, object : DatePickerHelper.Callback {
            override fun onDateSelected(dayofMonth: Int, month: Int, year: Int) {
                val dayStr = if (dayofMonth < 10) "0${dayofMonth}" else "${dayofMonth}"
                val mon = month + 1
                val monthStr = if (mon < 10) "0${mon}" else "${mon}"
                if (type.equals("book_start")) {
                    book_start_date.value = "${dayStr}-${monthStr}-${year}"


                } else if (type.equals("book_end")) {
                    book_end_date.value = "${dayStr}-${monthStr}-${year}"

                } else if (type.equals("search_start")) {
                    search_start_date.value = "${dayStr}-${monthStr}-${year}"

                } else if (type.equals("search_end")) {
                    search_end_date.value = "${dayStr}-${monthStr}-${year}"

                }
            }
        })
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()

        } else {
            CheckGpsStatus()

        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            RECORD_REQUEST_CODE
        )
    }


    fun CheckGpsStatus() {
        //checking the status of GPS sensor
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        GpsStatus = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (GpsStatus === true) {
            //incase GPS is enabled , we initialze the view model and subscribe the observers.
            mainactVm =
                ViewModelProvider(this, MyViewModelFactory()).get(MainActivityViewModel::class.java)
// performing get data action.
            mainactVm.setStateEvent(SubmitStatus.getData, null)
            datePicker = DatePickerHelper(this)

            setupApptList(appartments)
            subscribeObservers()
        } else {
            // if GPS is disabled we ask user to enable it
            UIHelper.showAlertDialog(
                "In order to calculate your distance from the available appartments, please enable your device's GPS",
                "Permission Request",
                this
            ).setPositiveButton(
                "Enable"
            ) { dialog, _ ->
                // taking user to the locations settings
                startActivityForResult(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 1);
            }.setCancelable(false).show()

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            RECORD_REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
// requesting user to grant permission.
                    makeRequest()

                } else {
                    // if the permission is granted , we need to check the GPS status.
                    CheckGpsStatus()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> {
                //coming back from the location settings

                CheckGpsStatus()
            }
        }
    }

    // all the click events are handled from here.
    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.et_fromdate -> {
                showDatePickerDialog("search_start")

            }
            R.id.et_todate -> {
                showDatePickerDialog("search_end")

            }
            R.id.btn_search -> {

                if (et_fromdate.text.toString().length == 0 || et_todate.text.toString().length == 0 || et_beds.text.toString().length == 0) {
                    UIHelper.showShortToastInCenter(this, "invalid input")
                    return

                }
                // performing search operation.
                mainactVm.setStateEvent(
                    SubmitStatus.Search,
                    Appartments(
                        "",
                        et_beds.text.toString().toInt(),
                        "",
                        0.0,
                        0.0,
                        true,
                        Utils.stringToDate(et_fromdate.text.toString()),
                        Utils.stringToDate(et_todate.text.toString()),
                        0.0

                    )
                )

            }
            //reseting the search
            R.id.btn_filter -> {
                et_fromdate.setText("")
                et_todate.setText("")
                et_beds.setText("")
                mainactVm.setStateEvent(SubmitStatus.getData, null)

            }


        }
    }
}