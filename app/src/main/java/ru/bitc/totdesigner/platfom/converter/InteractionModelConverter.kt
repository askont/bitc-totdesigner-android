package ru.bitc.totdesigner.platfom.converter

import com.tickaroo.tikxml.TikXml
import okio.Buffer
import ru.bitc.totdesigner.model.entity.interaction.Interaction
import ru.bitc.totdesigner.model.entity.interaction.PartImage
import ru.bitc.totdesigner.model.entity.interaction.Scene
import ru.bitc.totdesigner.model.models.Settings
import java.io.*
import java.lang.Exception

/**
 * Created on 13.03.2020
 * @author YWeber */

class InteractionModelConverter : ConverterXmlToModel<Settings> {
    private val xmlParser = TikXml.Builder()
        .exceptionOnUnreadXml(false)
        .build()

    override fun loadXmlToModel(file: File): Settings {
        val fileReader: Reader = BufferedReader(InputStreamReader(FileInputStream(file)))
        return xmlParser.read(Buffer().writeUtf8(fileReader.readText()), Settings::class.java)
    }

    fun convertToEntity(lessonPath: String, settings: Settings): Interaction {

        val scenes = settings.stages.map { stage ->
            Scene(
                stage.position,
                stage.description,
                absolutePath(lessonPath, stage.preview),
                stage.workItem
                    .map {
                        val asset = settings.findAsset(it.guid)
                        PartImage(
                            absolutePath(lessonPath, asset.preview),
                            asset.name,
                            it.isStatic,
                            it.transformation.m13,
                            it.transformation.m23,
                            it.transformation.m22,
                            it.transformation.m11
                        )
                    }

            )
        }
        return Interaction(scenes)
    }

    private fun Settings.findAsset(guid: String) = assets.first { it.guid == guid }

    private fun absolutePath(path: String, name: String): String = "$path${File.separator}$name"
}