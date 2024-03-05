package ade.test.danamon.ui

import ade.test.danamon.data.pref.UserPref
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExerciseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val defaultRoute =
            if (UserPref(context = this).user?.id.isNullOrEmpty()) Screen.Welcome.route
            else Screen.Home.route
        setContent {
            ExerciseApp(defaultRoute)
        }
    }
}