package ru.bitc.totdesigner.di

import org.koin.dsl.module
import ru.bitc.totdesigner.platfom.converter.InteractionModelConverter
import ru.bitc.totdesigner.platfom.converter.ModelLessonToEntityPreviewConverter
import ru.bitc.totdesigner.platfom.converter.SavedLessonModelConverter
import ru.bitc.totdesigner.system.ResourceManager
import ru.bitc.totdesigner.system.flow.AndroidDispatcher
import ru.bitc.totdesigner.system.flow.DispatcherProvider
import ru.bitc.totdesigner.system.notifier.ChangeBackgroundNotifier
import ru.bitc.totdesigner.system.notifier.DownloadNotifier
import ru.bitc.totdesigner.system.notifier.WindowsSizeNotifier
import ru.bitc.totdesigner.system.path.AndroidPathManager
import ru.bitc.totdesigner.system.path.PathManager
import ru.bitc.totdesigner.system.zip.UnpackingZip

/**
 * Created on 27.01.2020
 * @author YWeber */

object SystemModule {

    fun notifierModule() = module {
        single { DownloadNotifier() }
        single<DispatcherProvider> { AndroidDispatcher() }
        single { ResourceManager(get()) }
        single { ModelLessonToEntityPreviewConverter() }
        single { SavedLessonModelConverter() }
        single { InteractionModelConverter() }
        single<PathManager> { AndroidPathManager(get()) }
        single { UnpackingZip(get()) }
        single { WindowsSizeNotifier() }
        single { ChangeBackgroundNotifier() }
    }

}