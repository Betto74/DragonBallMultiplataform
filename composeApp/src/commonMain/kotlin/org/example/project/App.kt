package org.example.project

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import org.example.project.network.DragonBallApiClient
import org.example.project.ui.screens.CharacterListScreen

@Composable
fun App() {
    MaterialTheme {
        val apiClient = DragonBallApiClient()
        CharacterListScreen(apiClient)
    }
}
