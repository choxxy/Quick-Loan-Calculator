package tech.garageprojects.loancalculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProviders.of(this).get(MainViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        setupNavigation()

        MobileAds.initialize(this, "")

    }

    private fun setupNavigation() {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        setupWithNavController(toolbar, navController)
    }

    override fun onBackPressed() {

        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        if (navController.currentDestination!!.id == R.id.mainFragment) {
            finish()
        } else {
            navController.popBackStack()
        }
    }
}
