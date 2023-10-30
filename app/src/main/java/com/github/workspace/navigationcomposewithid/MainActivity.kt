package com.github.workspace.navigationcomposewithid

import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.os.BundleCompat
import androidx.core.os.bundleOf
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.workspace.navigationcomposewithid.ui.theme.NavigationcomposewithidTheme
import kotlinx.parcelize.Parcelize

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavigationcomposewithidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = "home"
                    ) {
                        composable(Destination.Home.route) {
                            val (firstName, setFirstName) = rememberSaveable {
                                mutableStateOf("")
                            }
                            val (lastName, setLastName) = rememberSaveable {
                                mutableStateOf("")
                            }

                            Scaffold(
                                topBar = {
                                    TopAppBar(
                                        title = { Text("Home") }
                                    )
                                }
                            ) {
                                Column(
                                    modifier = Modifier.padding(it)
                                        .padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    TextField(
                                        firstName,
                                        setFirstName,
                                        label = {
                                            Text("First name")
                                        }
                                    )
                                    TextField(
                                        lastName,
                                        setLastName,
                                        label = {
                                            Text("Last name")
                                        }
                                    )
                                    Button(
                                        onClick = {
                                            navController.navigate(
                                                resId = Destination.Result.id,
                                                args = bundleOf(
                                                    "user" to User(firstName, lastName)
                                                )
                                            )
                                        }
                                    ) {
                                        Text("Navigate with route id")
                                    }
                                }

                            }
                        }
                        composable(Destination.Result.route) { entry ->
                            Scaffold(
                                topBar = {
                                    TopAppBar(
                                        title = { Text("Result") }
                                    )
                                }
                            ) {
                                val args = entry.arguments ?: Bundle()
                                val user = BundleCompat.getParcelable(args, "user", User::class.java)
                                if (user != null) {
                                    Column(
                                        modifier = Modifier.padding(it)
                                            .padding(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        Text(text = user.toString())
                                        Text(text = user.firstName)
                                        Text(text = user.lastName)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

sealed class Destination(val route: String) {
    val id get() = "android-app://androidx.navigation/$route".hashCode()
    data object Home : Destination("home")
    data object Result : Destination("result")
}

@Parcelize
data class User(
    val firstName: String,
    val lastName: String
) : Parcelable
