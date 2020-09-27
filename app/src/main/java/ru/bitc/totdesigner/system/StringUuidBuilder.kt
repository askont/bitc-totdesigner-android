package ru.bitc.totdesigner.system

class StringUuidBuilder {
    fun buildPartId(path: String, itemX: String, itemY: String) = "$path@$itemX@$itemY"

    fun sliceCombineUuid(combineId: String): Triple<String, String, String> {
        val split = combineId.split("@")
        val partId = split[0]
        val partX = split[1]
        val partY = split[2]
        return Triple(partId, partX, partY)
    }
}