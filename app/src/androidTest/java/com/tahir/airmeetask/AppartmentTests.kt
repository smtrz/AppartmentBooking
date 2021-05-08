package com.tahir.airmeetask

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tahir.airmeetask.db.AppDB
import com.tahir.airmeetask.db.appartmentsDao
import com.tahir.airmeetask.models.Appartments
import com.tahir.airmeetask.viewstate.DataState
import com.tahir.airmeetask.vm.MainActivityViewModel
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import java.util.*
import java.util.concurrent.CountDownLatch
import kotlin.collections.ArrayList

@RunWith(AndroidJUnit4::class)

class AppartmentTests {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var daoAppartment: appartmentsDao
    private lateinit var db: AppDB
    lateinit var appartments: List<Appartments>
    lateinit var data_observer: Observer<DataState<List<Appartments>>>
    lateinit var viewModel: MainActivityViewModel


    @Before
    @Throws(Exception::class)
    fun setUp() {
        // setting up in memory database
        setupDb()


    }


    // testing appartment data insertion.
    @Test
    @Throws(InterruptedException::class)
    fun insertAppartmentSuccess() {
        //testing for insertion of appartment.
        assertEquals(insertAppartmentData(), 1)


    }


    fun insertAppartmentData(): Int {
        // inserting sample note object to the table.
        var data_insertion: List<Long>
        val sampleAppartment = ArrayList<Appartments>()
        sampleAppartment.add(Appartments("1", 2, "dummy", 64.232, 32.232, false, null, null, 0.0))


        runBlocking {
            data_insertion = daoAppartment.insertappartmentData(sampleAppartment.toList())

        }
        return data_insertion.size

    }

    fun bookAppartmentData(): Int {
        // update  sample appartment data  object to the table.
        var data_updation: Int

        runBlocking {
            data_updation = daoAppartment.bookAppartment(Date(), Date(), "1")

        }
        return data_updation

    }

    // testing update notes.
    @Test
    @Throws(InterruptedException::class)
    fun updateNoteSuccess() {
        // first inserting the row and the id will be 1
        insertAppartmentData()
// updating the same row using blocking operation.
        runBlocking {
            var note_updation = daoAppartment.bookAppartment(
                Date(),
                Date(),
                "1"


            )
            assertEquals(note_updation, 1)
        }


    }

    fun setupDb() {
// creating in memory database
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDB::class.java
        ).build()
        // getting thre distance of data access object from the database created.
        daoAppartment = db.appartmentDao()
    }

    @After
    @Throws(java.lang.Exception::class)
    fun tearDown() {
        // closing the database after it has been used.
        db.close()
        // removing the observer afterwards
        viewModel.dataState.removeObserver(data_observer)
    }

    // Testing ViewModel
    @Test
    @Throws(InterruptedException::class)
    fun AppartmentListNotEmpty() {
        //creating instance of viewmodel
        viewModel = MainActivityViewModel()
        val signal = CountDownLatch(1)

        data_observer = Observer<DataState<List<Appartments>>> { datastate ->
// once the data change has occured , latch should be decremented
            when (datastate) {


                is DataState.Success<List<Appartments>> -> {
                    appartments = datastate.data
                    signal.countDown()
                }


            }
            // observing dataState observer  forever till it has been removed
            viewModel.dataState.observeForever(data_observer)
            signal.await()
            Assert.assertTrue(appartments.count() > 0)


        }

    }
}