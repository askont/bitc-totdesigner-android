package ru.bitc.totdesigner.system

class StringUuidBuilder {
    fun buildPartId(path: String, itemX: String, itemY: String) = "$path@$itemX@$itemY"
}