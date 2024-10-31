package org.ktc2.cokaen.wouldyouin.feat_curation.view

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import org.ktc2.cokaen.wouldyouin.feat_curation.R

class CreateCurationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_curation)

    }
}