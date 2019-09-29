package com.karntrehan.talko

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.karntrehan.talko.extensions.Activities
import com.karntrehan.talko.extensions.intentTo

class TalkoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agni)

        startActivity(intentTo(Activities.Messages))
        finish()
    }
}
