package ru.bitc.totdesigner

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.bitc.totdesigner.platfom.ContentListFragment

class AppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val contentFragment = ContentListFragment(0)

        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.fragment_container,contentFragment)
        transaction.addToBackStack(null)

        transaction.commit()
    }
}
