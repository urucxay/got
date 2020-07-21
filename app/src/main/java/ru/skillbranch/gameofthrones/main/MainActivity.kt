package ru.skillbranch.gameofthrones.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.main.Status.*
import ru.skillbranch.gameofthrones.utils.EventObserver

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
//        viewModel.checkData()

        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme_NoActionBar)
        setContentView(R.layout.activity_main)
        initViewModel()
        viewModel.checkData()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun registerToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun initViewModel() {
        viewModel.showSnackBarEvent.observe(this, EventObserver { showSnackBar() })
        viewModel.status.observe(this, Observer { status ->
            Log.d("LOAD_DATA!!", "$status")
            when (status) {
                LOADING -> {
//                    container.visibility = View.GONE
                }
                SUCCESS -> {
                    container.visibility = View.VISIBLE
                }
                ERROR -> viewModel.showSnackBar()
            }
        })
    }

    private fun showSnackBar() {
        Snackbar.make(
            container,
            resources.getString(R.string.network_error),
            Snackbar.LENGTH_INDEFINITE
        )
    }

}
