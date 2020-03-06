package ru.bitc.totdesigner.model.database.core

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.bitc.totdesigner.model.database.dao.PathDao
import ru.bitc.totdesigner.model.database.dto.LessonPath

/**
 * Created on 03.03.2020
 * @author YWeber */

@Database(entities = [LessonPath::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun createPathDao(): PathDao
}