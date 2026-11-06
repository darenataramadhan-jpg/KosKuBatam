package com.example.kosku.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kosku.ui.screens.BookingScreen
import com.example.kosku.ui.screens.DashboardScreen
import com.example.kosku.ui.screens.DetailScreen
import com.example.kosku.ui.screens.FavoritesScreen
import com.example.kosku.ui.screens.HistoryScreen
import com.example.kosku.ui.screens.LoginScreen
import com.example.kosku.ui.screens.SplashScreen
import com.example.kosku.viewmodel.AuthViewModel
import com.example.kosku.viewmodel.KosViewModel

object Routes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val DASHBOARD = "dashboard"
    const val DETAIL = "detail/{kosId}"
    const val BOOKING = "booking/{kosId}"
    const val FAVORITES = "favorites"
    const val HISTORY = "history"
    fun detail(id: Int) = "detail/$id"
    fun booking(id: Int) = "booking/$id"
}

@Composable
fun AppNav(
    authVm: AuthViewModel = viewModel(),
    kosVm: KosViewModel = viewModel()
) {
    val nav = rememberNavController()
    val snackbar = remember { SnackbarHostState() }
    val auth by authVm.state.collectAsStateWithLifecycle()

    // Semua pesan dari ViewModel ditampilkan sebagai Snackbar
    LaunchedEffect(Unit) {
        kosVm.events.collect { msg ->
            snackbar.currentSnackbarData?.dismiss()
            snackbar.showSnackbar(msg)
        }
    }

    NavHost(navController = nav, startDestination = Routes.SPLASH) {

        composable(Routes.SPLASH) {
            SplashScreen(onFinish = {
                val target = if (auth.isLoggedIn) Routes.DASHBOARD else Routes.LOGIN
                nav.navigate(target) { popUpTo(Routes.SPLASH) { inclusive = true } }
            })
        }

        composable(Routes.LOGIN) {
            LoginScreen(
                authVm = authVm,
                onLoginSuccess = {
                    nav.navigate(Routes.DASHBOARD) { popUpTo(Routes.LOGIN) { inclusive = true } }
                }
            )
        }

        composable(Routes.DASHBOARD) {
            DashboardScreen(
                vm = kosVm,
                userName = auth.userName,
                snackbar = snackbar,
                onDetail = { nav.navigate(Routes.detail(it)) },
                onFavorites = { nav.navigate(Routes.FAVORITES) },
                onHistory = { nav.navigate(Routes.HISTORY) },
                onLogout = {
                    authVm.logout()
                    nav.navigate(Routes.LOGIN) { popUpTo(nav.graph.id) { inclusive = true } }
                }
            )
        }

        composable(
            Routes.DETAIL,
            arguments = listOf(navArgument("kosId") { type = NavType.IntType })
        ) { entry ->
            val id = entry.arguments?.getInt("kosId") ?: -1
            DetailScreen(
                kos = kosVm.findKos(id),
                vm = kosVm,
                snackbar = snackbar,
                onBack = { nav.popBackStack() },
                onBook = { nav.navigate(Routes.booking(id)) }
            )
        }

        composable(
            Routes.BOOKING,
            arguments = listOf(navArgument("kosId") { type = NavType.IntType })
        ) { entry ->
            val id = entry.arguments?.getInt("kosId") ?: -1
            BookingScreen(
                kos = kosVm.findKos(id),
                vm = kosVm,
                snackbar = snackbar,
                onBack = { nav.popBackStack() },
                onSuccess = {
                    nav.navigate(Routes.HISTORY) { popUpTo(Routes.DASHBOARD) }
                }
            )
        }

        composable(Routes.FAVORITES) {
            FavoritesScreen(
                vm = kosVm,
                snackbar = snackbar,
                onBack = { nav.popBackStack() },
                onDetail = { nav.navigate(Routes.detail(it)) }
            )
        }

        composable(Routes.HISTORY) {
            HistoryScreen(vm = kosVm, snackbar = snackbar, onBack = { nav.popBackStack() })
        }
    }
}
