package ru.bitc.totdesigner.model.database.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.File

/**
 * Created on 03.03.2020
 * @author YWeber */

@Entity(tableName = "lesson_path")
data class LessonPath(
    @PrimaryKey
    @ColumnInfo(name = "remote_url")
    val lessonRemoteUrl: String,
    @ColumnInfo(name = "local_path")
    val lessonLocalPath: String
)

fun LessonPath.absolutePath(name: String): String {
    return lessonLocalPath + File.separator + name
}
