package com.example.creadorform.datos

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import java.io.IOException


class GestorArchivos(private val context: Context) {

    fun guardarForm(
        nombreArchivo: String,
        contenido: String,
        carpetaDestinoUri: Uri? = null
    ): Boolean {
        return guardarTexto(nombreArchivo, contenido, carpetaDestinoUri)
    }

    fun obtenerForm(
        nombreArchivo: String,
        carpetaDestinoUri: Uri? = null
    ): String {
        return leerTexto(nombreArchivo, carpetaDestinoUri)
    }

    fun guardarPKM(
        nombreArchivo: String,
        contenido: String,
        carpetaDestinoUri: Uri? = null
    ): Boolean {
        return guardarTexto(nombreArchivo, contenido, carpetaDestinoUri)
    }

    fun obtenerPKM(
        nombreArchivo: String,
        carpetaDestinoUri: Uri? = null
    ): String {
        return leerTexto(nombreArchivo, carpetaDestinoUri)
    }

    private fun guardarTexto(
        nombreArchivo: String,
        contenido: String,
        carpetaDestinoUri: Uri?
    ): Boolean {
        return try {
            if (carpetaDestinoUri == null) {
                context.openFileOutput(nombreArchivo, Context.MODE_PRIVATE).use { out ->
                    out.write(contenido.toByteArray(Charsets.UTF_8))
                }
                true
            } else {
                val carpeta = DocumentFile.fromTreeUri(context, carpetaDestinoUri) ?: return false
                if (!carpeta.isDirectory) return false

                val existente = carpeta.findFile(nombreArchivo)
                val archivo = existente ?: carpeta.createFile("application/octet-stream", nombreArchivo) ?: return false

                context.contentResolver.openOutputStream(archivo.uri, "wt")?.use { out ->
                    out.write(contenido.toByteArray(Charsets.UTF_8))
                } ?: return false

                true
            }
        } catch (_: IOException) {
            false
        } catch (_: SecurityException) {
            false
        }
    }

    private fun leerTexto(
        nombreArchivo: String,
        carpetaDestinoUri: Uri?
    ): String {
        return try {
            if (carpetaDestinoUri == null) {
                context.openFileInput(nombreArchivo).bufferedReader(Charsets.UTF_8).use { it.readText() }
            } else {
                val carpeta = DocumentFile.fromTreeUri(context, carpetaDestinoUri) ?: return ""
                val archivo = carpeta.findFile(nombreArchivo) ?: return ""
                context.contentResolver.openInputStream(archivo.uri)?.bufferedReader(Charsets.UTF_8)?.use { it.readText() }
                    ?: ""
            }
        } catch (_: IOException) {
            ""
        } catch (_: SecurityException) {
            ""
        }
    }
}
