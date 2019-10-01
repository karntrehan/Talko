package com.karntrehan.talko.messages

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.karntrehan.talko.TalkoApp
import com.karntrehan.talko.messages.di.MessagesDH
import com.karntrehan.talko.messages.landing.MessagesFragment

class MessagesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)

        initMessagesDI()

        displayFragment(MessagesFragment.newInstance(), MessagesFragment.TAG)

    }

    private fun initMessagesDI() {
        if (application is TalkoApp)
            MessagesDH.init(application as TalkoApp)
    }

    private fun displayFragment(fragment: Fragment, tag: String) {
        if (supportFragmentManager.findFragmentByTag(tag) != null) return

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.flContainer, fragment, tag)
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1)
            super.onBackPressed()
        else finish()
    }
}
