package org.jetbrains.plugins.template

import com.google.genai.Client
import com.intellij.lang.annotation.*
import com.intellij.psi.*

class FriendlyAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {

        if (element !is PsiErrorElement) return

        val errorOriginal = element.errorDescription
        val ajustes = PluginSettings.getInstance()
        var esErrorDeLlave = false
        var mensajeFinal = ""

        // 1. ELS TEUS 4 ERRORS PREDETERMINATS INTACTES
        if (errorOriginal.contains(";")) {
            mensajeFinal = ajustes.mensajePuntoYComa
        }
        else if (errorOriginal.contains(")")) {
            mensajeFinal = ajustes.mensajeParentesisCerrar
        }
        else if (errorOriginal.contains("}")) {
            esErrorDeLlave = true
            mensajeFinal = ajustes.mensajeCorxeteCerrar
        }
        else if (errorOriginal.contains("]")) {
            mensajeFinal = ajustes.mensajeClaudatorCerrar
        }
        // 2. SI ÉS UN ALTRE ERROR, CRIDEM A LA IA
        else {
            try {
                // 🚨 COMPRUEBA ESTO: Posa la teva NOVA API Key aquí (NO FACIS GIT PUSH!)
                val miApiKey = System.getenv("GEMINI_API_KEY")

                val client = Client.builder()
                    .apiKey(miApiKey)
                    .build()

                // Li demanem a la IA que ens expliqui l'error (Max 3 línies)
                val prompt = "Tengo este error en Java: $errorOriginal. Explícame de forma clara, muy entendible para todas las personas y cómo lo arreglo. En un maximo de 5 linias"

                val response = client.models.generateContent(
                    "gemma-4-26b-a4b-it",
                    prompt,
                    null
                )

                val respuestaIA = response.text() ?: "Sin respuesta de la IA."

                // Ho seguim imprimint a la terminal per al teu control intern
                println("\n==================================================")
                println("🤖 SOLUCIÓN DE LA IA PARA TU ERROR:")
                println(respuestaIA)
                println("==================================================\n")

                // Ara a la pantalla es mostrarà l'explicació de la IA directament
                mensajeFinal = "🤖 IA: $respuestaIA"

            } catch (e: Exception) {
                // Si falla, et dirà EXACTAMENT per què a la terminal
                println("\n❌ ERROR DE CONEXIÓN CON LA IA ❌")
                println("Motivo exacto: ${e.message}")
                e.printStackTrace()
                mensajeFinal = "🤖 Error IA de connexió. Revisa la consola."
            }
        }

        // Pintem el missatge a l'editor
        if (esErrorDeLlave && element.textLength == 0) {
            holder.newAnnotation(HighlightSeverity.ERROR, mensajeFinal)
                .range(element.textRange)
                .afterEndOfLine()
                .create()
        } else {
            holder.newAnnotation(HighlightSeverity.ERROR, mensajeFinal)
                .range(element.textRange)
                .create()
        }
    }
}