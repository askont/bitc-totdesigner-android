package ru.bitc.totdesigner.model.entity

/*
 * Created on 2019-12-20
 * @author YWeber
 */
data class PreviewLessons(val previews: List<Lesson>) {

    data class Lesson(
        val title: String,
        val imageUrl: String,
        val category: Category
    )

    enum class Category(val category: String) {
        PAID("Paid"), FREE("Free"), UNKNOW("")
    }

}