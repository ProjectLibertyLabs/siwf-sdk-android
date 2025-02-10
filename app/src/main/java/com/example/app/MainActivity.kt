package com.example.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.liwl.LiwlButton
import com.example.liwl.LiwlButtonStyle

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Surface(Modifier.padding(16.dp)) {
                Text(
                    text = "Demo App",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(top = 50.dp)
                )
                Column(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    LiwlButton(
                        redirectUrl = "https://www.google.com"
                    )
                    LiwlButton(
                        style = LiwlButtonStyle.DARK,
                        title = "No Link"
                    )
                    LiwlButton(
                        style = LiwlButtonStyle.LIGHT,
                        redirectUrl = "https://testnet.frequencyaccess.com"
                    )
                }
            }
        }
    }
}
