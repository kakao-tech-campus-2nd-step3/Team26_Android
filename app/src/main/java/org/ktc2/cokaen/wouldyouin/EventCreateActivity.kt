package org.ktc2.cokaen.wouldyouin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import org.ktc2.cokaen.wouldyouin.databinding.ActivityEventCreateBinding

class EventCreateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEventCreateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_event_create)
    }
}