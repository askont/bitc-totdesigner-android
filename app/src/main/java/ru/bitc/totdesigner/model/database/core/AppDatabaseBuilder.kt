package ru.bitc.totdesigner.model.database.core

import android.content.Context
import androidx.room.Room

/**
 * Created on 03.03.2020
 * @author YWeber */

object AppDatabaseBuilder {

    fun createDatabase(context: Context) = Room
        .databaseBuilder(context, AppDatabase::class.java, "database")
        .build()

    fun createPathDao(appDatabase: AppDatabase) = appDatabase.createPathDao()

}