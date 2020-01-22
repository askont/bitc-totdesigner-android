package ru.bitc.totdesigner.platfom

import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import ru.terrakok.cicerone.Navigator

/**
 * Created on 21.01.2020
 * @author YWeber */

abstract class BaseActivity(@LayoutRes private val layoutRes: Int) : AppCompatActivity() {

    abstract val viewModel: BaseViewModel
    abstract val navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (layoutRes != -1) {
            setContentView(layoutRes)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.addNavigator(navigator)
    }

    override fun onPause() {
        viewModel.deleteNavigator()
        super.onPause()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home && onBack()) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        supportFragmentManager.fragments.lastOrNull {
            (it as? BaseFragment)?.onBackPressed() ?: false
        } ?: onBack()
    }

    open fun onBack(): Boolean {
        super.onBackPressed()
        return false
    }

}