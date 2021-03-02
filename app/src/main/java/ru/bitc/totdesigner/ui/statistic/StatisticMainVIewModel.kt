package ru.bitc.totdesigner.ui.statistic

import ru.bitc.totdesigner.platfom.BaseActionViewModel
import ru.bitc.totdesigner.platfom.adapter.state.StatisticItem
import ru.bitc.totdesigner.ui.statistic.state.StatisticState

class StatisticMainVIewModel :
    BaseActionViewModel<StatisticState>(initState = StatisticState.EMPTY) {


    init {
        action.value = currentState.copy(
            "Экономика. Маркетинг",
            "https://s3.eu-central-1.amazonaws.com/totdesigner/previews/%D0%9C%D0%B0%D1%80%D0%BA%D0%B5%D1%82%D0%B8%D0%BD%D0%B3.png",
            10, 3, 5, listOf(
                StatisticItem(
                    false,
                    "Пользовательское взаимодействие UX для социальной сети",
                    "Этап 1",
                    "10%",
                    "1 из 10"
                ), StatisticItem(
                    false,
                    "Пользовательское взаимодействие UX для музыкального плеера",
                    "Этап 2",
                    "50%",
                    "5 из 10"
                ), StatisticItem(
                    true,
                    "Пользовательское взаимодействие UX для покупки и оплата",
                    "Этап 3",
                    "100%",
                    "10 из 10"
                ), StatisticItem(
                    false,
                    "Пользовательское взаимодействие UX для приложения заметки",
                    "Этап 4",
                    "10%",
                    "5 из 50"
                ), StatisticItem(
                    true,
                    "Пользовательское взаимодействие UX для социальной сети",
                    "Этап 5",
                    "100%",
                    "100 из 100"
                ), StatisticItem(
                    false,
                    "Элементы управления UI KIT Социальные сети, загрузка, предлж…",
                    "Этап 6",
                    "74%",
                    "8 из 13"
                ), StatisticItem(
                    false,
                    "Пользовательское взаимодействие UX для социальной сети",
                    "Этап 7",
                    "10%",
                    "5 из 10"
                )
            )
        )
    }

}