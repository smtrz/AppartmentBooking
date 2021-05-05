package com.tahir.airmeetask.db


import android.content.Context
import com.tahir.airmeetask.app.App
import java.util.*
import javax.inject.Inject


class DbRepository {
    @Inject
    lateinit var appartmentsDao: appartmentsDao

    @Inject
    lateinit var c: Context

    init {
        // injecting Dagger component
        App.app.appLevelComponent.inject(this@DbRepository)

    }

    /*
       function getNoteData gets all notes from database table
        */
    /* fun getNoteData(): LiveData<List<MyNotes>> {
         return myNotes.getAllNotes()

     }
     *//*
   function deleteNoteData deletes selected note id from DB table and return the row count
      *//*

    suspend fun deleteNoteData(Id: Int): Int {
        return myNotes.deleteSelectedNote(Id)

    }

    *//*
       function editNoteData updates the note into DB table and return the row count
        *//*
    suspend fun editNoteData(
        title: String,
        desc: String,
        imageUrl: String,
        note_id: Int,
        editOn: String
    ): Int {
        val row = myNotes.editSelectedNote(title, desc, imageUrl, note_id, editOn)
        return row
    }

    *//*
   function insertnewNote  add new note into DB table and return the row count
     *//*
    suspend fun insertnewNote(note: MyNotes): Long {
        return withContext(Dispatchers.IO) {
            myNotes.insertNotes(note)

        }

        // return row
    }*/


}
