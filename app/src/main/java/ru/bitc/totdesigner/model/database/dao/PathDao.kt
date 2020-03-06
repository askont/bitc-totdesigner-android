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
    fun findLessonPath(remotePath: String): LessonPath

    @Delete
    fun deletePath(remotePath: LessonPath)
}