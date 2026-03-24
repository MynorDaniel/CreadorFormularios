package com.example.creadorform.datos

import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class PokeAPI {
    fun obtenerNombresPokemon(inicio: Int, fin: Int): Array<String>{
        if (inicio !in 0..fin) {
            throw IllegalArgumentException("Rango inválido en who_is_that_pokemon")
        }

        val limit = fin - inicio + 1
        val offset = inicio - 1

        val endpoint = "https://pokeapi.co/api/v2/pokemon?limit=$limit&offset=$offset"

        val connection = (URL(endpoint).openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            connectTimeout = 10_000
            readTimeout = 10_000
            doInput = true
        }

        return try {
            val code = connection.responseCode
            if (code !in 200..299) {
                throw IOException("Error HTTP $code al consultar PokeAPI")
            }

            val body = connection.inputStream.bufferedReader().use { it.readText() }
            val json = JSONObject(body)
            val results = json.getJSONArray("results")

            Array(results.length()) { i ->
                results.getJSONObject(i).getString("name")
            }
        } finally {
            connection.disconnect()
        }
    }
}