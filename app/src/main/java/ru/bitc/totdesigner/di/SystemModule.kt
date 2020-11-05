package ru.bitc.totdesigner.di

import org.koin.dsl.module
import ru.bitc.totdesigner.platfom.converter.InteractionModelConverter
import ru.bitc.totdesigner.platfom.converter.ModelLessonToEntityPreviewConverter
import ru.bitc.totdesigner.platfom.converter.SavedLessonModelConverter
import ru.bitc.totdesigner.platfom.mapper.SceneStateMapper
import ru.bitc.totdesigner.system.ResourceManager
import ru.bitc.totdesigner.system.flow.AndroidDispatcher
import ru.bitc.totdesigner.system.flow.DispatcherProvider
import ru.bitc.totdesigner.system.notifier.ChangeBackgroundNotifier
import ru.bitc.totdesigner.system.notifier.DownloadNotifier
import ru.bitc.totdesigner.system.notifier.WindowsSizeNotifier
import ru.bitc.totdesigner.system.path.AndroidPathManager
import ru.bitc.totdesigner.system.path.PathManager
import ru.bitc.totdesigner.system.zip.UnpackingZip
import ru.bitc.totdesigner.ui.setting.state.SettingPlatform

/**
 * Created on 27.01.2020
 * @author YWeber */

object SystemModule {

    fun notifierModule() = module {
        single { DownloadNotifier() }
        single<DispatcherProvider> { AndroidDispatcher() }
        single { ResourceManager(context = get()) }
        single { ModelLessonToEntityPreviewConverter() }
        single { SavedLessonModelConverter() }
        single { InteractionModelConverter() }
        single<PathManager> { AndroidPathManager(context = get()) }
        single { UnpackingZip(pathManager = get()) }
        single { WindowsSizeNotifier() }
        single { ChangeBackgroundNotifier() }
        // mapper
        single { SceneStateMapper(uuidBuilder = get()) }
        single { SettingPlatform(prefsStore = get(), resourceManager = get()) }
    }

}