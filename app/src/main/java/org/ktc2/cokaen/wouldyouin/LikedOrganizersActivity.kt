package org.ktc2.cokaen.wouldyouin

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import org.ktc2.cokaen.wouldyouin.databinding.ActivityLikedOrganizersBinding

class LikedOrganizersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLikedOrganizersBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_liked_organizers)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(
            R.menu.menu_liked_organizers,
            menu
        )
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.notification_button -> {
                
                val intent = Intent(this@LikedOrganizersActivity, NotificationsActivity::class.java)
                startActivity(intent)

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}