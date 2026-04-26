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

        // Comprobamos los errores de sintaxis predeterminados
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
        // Si es un error distinto, consultamos la solución a la IA
        else {
            try {
                // Inicializamos el cliente
                val miApiKey = System.getenv("GEMINI_API_KEY")

                val client = Client.builder()
                    .apiKey(miApiKey)
                    .build()

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

        // Mostramos la anotación en el editor
        if (esErrorDeLlave && element.textLength == 0) {
            // Anotación para errores de llaves al final de la línea
            holder.newAnnotation(HighlightSeverity.ERROR, mensajeFinal)
                .range(element.textRange)
                .afterEndOfLine()
                .create()
        } else {
            // Anotación con globo flotante (Tooltip HTML) e imagen aleatoria
            val urlImagen = FriendlyAnnotator::class.java.getResource("/icons/icon${(1..5).random()}.jpg")

            // Formateamos el texto de la IA para que sea compatible con HTML
            val textoIAParaHtml = mensajeFinal
                .replace("\n", "<br>")
                .replace(Regex("\\*\\*(.*?)\\*\\*"), "<b>$1</b>")

            val mensajeHtml = """
                <html>
                <body style="background-color: #2b2b2b; color: #a9b7c6; font-family: sans-serif; padding: 15px;">
                    <div style="text-align: center;">
                        <img src="$urlImagen" width="200" height="150"><br>
                        
                        <div style="margin-top: 10px;">
                            <h3 style="color: #ff5647; margin: 0 0 5px 0;">¡BOOM! 💥</h3>
                            $textoIAParaHtml
                        </div>
                    </div>
                </body>
                </html>
            """.trimIndent()

            holder.newAnnotation(HighlightSeverity.ERROR, mensajeFinal)
                .tooltip(mensajeHtml)
                .range(element.textRange)
                .create()
        }
    }
}