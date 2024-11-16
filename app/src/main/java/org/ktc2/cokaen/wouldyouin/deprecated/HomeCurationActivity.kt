package org.ktc2.cokaen.wouldyouin.deprecated

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import org.ktc2.cokaen.wouldyouin.R
import org.ktc2.cokaen.wouldyouin.databinding.ActivityHomeCurationBinding

class HomeCurationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeCurationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_curation)
    }
}