package ba.etf.rma23.projekat

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    private lateinit var navView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        val orientation = resources.configuration.orientation
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController
            navView = findViewById(R.id.bottomNavigation)
            navView.setupWithNavController(navController)
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.home_fragment_container, HomeFragment())
                .replace(R.id.details_fragment_container, GameDetailsFragment()).commit()
        }
    }
}
