package org.example.project.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.project.model.Character
import org.example.project.network.DragonBallApiClient
import org.example.project.ui.components.CharacterCard

@Composable
fun CharacterListScreen(apiClient: DragonBallApiClient) {
    var allCharacters by remember { mutableStateOf<List<Character>>(emptyList()) }
    var currentCharacters by remember { mutableStateOf<List<Character>>(emptyList()) }
    var isLoadingPage by remember { mutableStateOf(true) }
    var isLoadingAll by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var currentPage by remember { mutableStateOf(1) }
    var totalPages by remember { mutableStateOf(1) }
    var lastPageBeforeSearch by remember { mutableStateOf(1) }

    // Cargar página actual con paginación
    LaunchedEffect(currentPage) {
        isLoadingPage = true
        errorMessage = null
        try {
            val response = apiClient.getCharacters(page = currentPage, limit = 10)
            currentCharacters = response.items
            totalPages = response.meta.totalPages
        } catch (e: Exception) {
            errorMessage = e.message
            currentCharacters = emptyList()
        }
        isLoadingPage = false
    }

    // Cargar todos los personajes solo una vez en background para búsqueda global
    LaunchedEffect(Unit) {
        isLoadingAll = true
        try {
            allCharacters = apiClient.getAllCharacters()
        } catch (e: Exception) {
            // Puedes manejar error separado si quieres
        }
        isLoadingAll = false
    }

    // Filtrar personajes para búsqueda (sobre todo cuando searchQuery no está vacío)
    val filteredCharacters = if (searchQuery.isBlank()) {
        currentCharacters
    } else {
        allCharacters.filter {
            it.name.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { newValue ->
                if (searchQuery.isBlank() && newValue.isNotBlank()) {
                    lastPageBeforeSearch = currentPage
                }
                if (searchQuery.isNotBlank() && newValue.isBlank()) {
                    currentPage = lastPageBeforeSearch
                }
                searchQuery = newValue
            },
            label = { Text("Buscar personaje") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            singleLine = true,
            shape = RoundedCornerShape(16.dp)
        )

        when {
            isLoadingPage && searchQuery.isBlank() -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            errorMessage != null -> Text(
                text = "Error: $errorMessage",
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.error
            )
            filteredCharacters.isEmpty() -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No se encontraron personajes")
            }
            else -> LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredCharacters) { character ->
                    CharacterCard(character)
                }
            }
        }

        // Mostrar botones solo si no hay búsqueda activa
        if (searchQuery.isBlank()) {
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { if (currentPage > 1) currentPage-- },
                    enabled = currentPage > 1,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Anterior")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    "Página $currentPage de $totalPages",
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = { if (currentPage < totalPages) currentPage++ },
                    enabled = currentPage < totalPages,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Siguiente")
                }
            }
        }
    }
}
