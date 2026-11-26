package com.example.qnbanimation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.qnbanimation.ui.theme.QNBAnimationTheme
import com.example.qnbanimation.ui.screens.QnbWalletScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QNBAnimationTheme {
                QnbWalletScreen(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
