package ru.bitc.totdesigner

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.bitc.totdesigner.main.MainFragment

class AppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .replace(R.id.appContainer, MainFragment.newInstance())
            .commit()
    }
}
