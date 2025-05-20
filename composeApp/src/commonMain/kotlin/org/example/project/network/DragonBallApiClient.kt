package org.example.project.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.example.project.model.Character
import org.example.project.model.CharactersResponse

class DragonBallApiClient {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true // Ignora campos JSON no modelados
            })
        }
    }

    /**
     * Obtiene la lista de personajes paginada.
     * @param page número de página (por defecto 1)
     * @param limit cantidad de personajes por página (por defecto 10)
     */
    suspend fun getCharacters(page: Int = 1, limit: Int = 10): CharactersResponse {
        val url = "https://dragonball-api.com/api/characters?page=$page&limit=$limit"
        return client.get(url).body()
    }

    suspend fun getAllCharacters(): List<Character> {
        val url = "https://dragonball-api.com/api/characters?limit=1000"
        return client.get(url).body<CharactersResponse>().items
    }

}
