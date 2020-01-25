package ru.bitc.totdesigner.platfom.navigation

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import ru.terrakok.cicerone.android.support.SupportAppNavigator

/**
 * Created on 21.01.2020
 * @author YWeber */
class ActivityNavigatorProxy(activity: FragmentActivity?, @IdRes containerId: Int = -1) :
    SupportAppNavigator(activity, containerId)