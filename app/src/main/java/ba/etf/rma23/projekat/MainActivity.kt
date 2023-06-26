package ba.etf.rma23.projekat

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import ba.etf.rma23.projekat.data.repositories.AppDatabase
import ba.etf.rma23.projekat.data.repositories.GameReview
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var navView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        //sendReview()      // ubacuje random review u bazu kako bi se ista kreirala.
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

    fun sendReview() {
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        scope.launch {
            var db = AppDatabase.getInstance(applicationContext)
            db.gameDao().insert(GameReview(2, "a", 125, false, "", "", 1562233))
        }
    }
}
