package ru.bitc.totdesigner

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.bitc.totdesigner.platfom.ContentListFragment

class AppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val contentFragment = ContentListFragment(0)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, contentFragment)
            .addToBackStack(null)
            .commit()
    }
}
