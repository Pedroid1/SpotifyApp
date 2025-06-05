package com.pedroid.spotifyapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.forEach
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptions
import androidx.navigation.createGraph
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pedroid.navigation.NavigationNode
import com.pedroid.navigation.features.HomeNavigation
import com.pedroid.navigation.features.PlaylistNavigation
import com.pedroid.spotifyapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var navigationNodes: @JvmSuppressWildcards Set<NavigationNode>

    private lateinit var navController: NavController

    private lateinit var _binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initialWork()
    }

    private fun initialWork() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        setupNavGraph()
        initBottomSheet()
        setupWithNavController(_binding.navView, navController)
    }

    private fun setupWithNavController(
        navView: BottomNavigationView,
        navController: NavController
    ) {
        navView.setOnItemSelectedListener { item ->
            onNavDestinationSelected(item, navController)
        }

        val weakReference = WeakReference(navView)
        navController.addOnDestinationChangedListener(
            object : NavController.OnDestinationChangedListener {
                override fun onDestinationChanged(
                    controller: NavController,
                    destination: NavDestination,
                    arguments: Bundle?,
                ) {
                    weakReference.get()?.let { view ->
                        handleDestinationChange(view, destination)
                    } ?: run {
                        navController.removeOnDestinationChangedListener(this)
                    }
                }
            },
        )
    }

    private fun handleDestinationChange(
        view: BottomNavigationView,
        destination: NavDestination
    ) {
        view.menu.forEach { item ->
            BOTTOM_NAV_MAPPING.find { pair -> item.itemId == pair.first }?.let { routePair ->
                if (destination.route == routePair.second) {
                    item.isChecked = true
                }
            }
        }
    }

    @Suppress("SwallowedException")
    private fun onNavDestinationSelected(item: MenuItem, navController: NavController): Boolean {
        val route = BOTTOM_NAV_MAPPING.find { pair -> item.itemId == pair.first }?.second
        if (route.isNullOrBlank()) return false

        val builder = NavOptions.Builder().setLaunchSingleTop(true).setRestoreState(false)
        if (item.order and Menu.CATEGORY_SECONDARY == 0) {
            builder.setPopUpTo(
                navController.graph.findStartDestination().route,
                inclusive = false,
                saveState = false
            )
        }
        val options = builder.build()
        return try {
            navController.navigate(route, options)
            navController.currentDestination?.route == route
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    private fun initBottomSheet() {
        _binding.navView.menu.apply {
            add(
                Menu.CATEGORY_CONTAINER,
                R.id.home_route,
                0,
                getString(R.string.home_bottom_nav_title)
            ).apply {
                icon = AppCompatResources.getDrawable(this@MainActivity, R.drawable.disco_menu)
                isCheckable = true
            }
            add(
                Menu.CATEGORY_CONTAINER,
                R.id.playlists_route,
                1,
                getString(R.string.playlists_bottom_nav_title)
            ).apply {
                icon = AppCompatResources.getDrawable(this@MainActivity, R.drawable.play_menu)
                isCheckable = true
            }
            add(
                Menu.CATEGORY_CONTAINER,
                R.id.profile_route,
                2,
                getString(R.string.profile_bottom_nav_title)
            ).apply {
                icon = AppCompatResources.getDrawable(this@MainActivity, R.drawable.user_menu)
                isCheckable = true
            }
        }
    }

    private fun setupNavGraph() {
        navController.graph = navController.createGraph(
            startDestination = HomeNavigation.ROUTE
        ) {
            navigationNodes.forEach { navNode ->
                navNode.addNode(this)
            }
        }
    }

    companion object {
        val BOTTOM_NAV_MAPPING = listOf(
            R.id.home_route to HomeNavigation.Destination.Home.route,
            R.id.playlists_route to PlaylistNavigation.Destination.Playlist.route
        )
    }
}
