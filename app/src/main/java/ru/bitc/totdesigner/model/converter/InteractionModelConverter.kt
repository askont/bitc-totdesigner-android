package ru.bitc.totdesigner.model.converter

import com.tickaroo.tikxml.TikXml
import okio.Buffer
import ru.bitc.totdesigner.model.entity.interaction.Interaction
import ru.bitc.totdesigner.model.models.Settings
import java.io.*

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
        val partImages = settings.assets.map {
            Interaction.Part(lessonPath + File.separator + it.preview, it.name)
        }
        val previews = settings.stages.map {
            Interaction.Preview(lessonPath + File.separator + it.preview, it.position)
        }
        return Interaction(partImages, previews)
    }

}