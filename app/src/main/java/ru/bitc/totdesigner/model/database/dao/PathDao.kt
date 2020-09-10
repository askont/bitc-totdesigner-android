package ru.bitc.totdesigner.model.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import ru.bitc.totdesigner.model.database.dto.LessonPath

/**
 * Created on 03.03.2020
 * @author YWeber */
@Dao
interface PathDao {
    @Query("SELECT * FROM lesson_path")
    suspend fun gelAllPath(): List<LessonPath>

    @Insert(onConflict = REPLACE)
    suspend fun insertPath(lessonPath: LessonPath)

    @Query("SELECT * FROM lesson_path WHERE remote_url = :remotePath")
    suspend fun findLessonPath(remotePath: String): LessonPath

    @Query("SELECT * FROM lesson_path WHERE lesson_name = :lessonName")
    suspend fun findLessonByName(lessonName: String): LessonPath?

    @Delete
    suspend fun deletePath(remotePath: LessonPath)
}