package ru.bitc.totdesigner.model.repository

import ru.bitc.totdesigner.model.database.dao.PathDao
import ru.bitc.totdesigner.system.flow.DispatcherProvider

/**
 * Created on 03.03.2020
 * @author YWeber */

class HomeLessonRepository(
    private val pathDao: PathDao,
    private val dispatcher: DispatcherProvider
) {

}