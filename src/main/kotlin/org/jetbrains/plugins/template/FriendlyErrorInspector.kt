package org.jetbrains.plugins.template
import com.google.genai.Client
import com.google.genai.types.GenerateContentResponse
import com.intellij.lang.annotation.*
import com.intellij.psi.*


class FriendlyAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        val client = Client.builder()
            .apiKey("AIzaSyCujA2q9rC-JwWKwla0EofbYBJkKEB-pQM")
            .build()
        val response = client.models.generateContent(
            "gemma-4-26b-a4b-it",
            "I need help",
            null
        )

        println(response.text())

        // --- TRAMPES VISUALS PER LA DEMO (Perquè surti la IA en errors de lògica) ---
        val textoElemento = element.text ?: ""

        if (textoElemento == "''") {
            holder.newAnnotation(HighlightSeverity.ERROR, "¡Llamando a la IA!...")
                .range(element.textRange)
                .create()
            return
        }

        if ((element.javaClass.simpleName.contains("LocalVariable") || element.javaClass.simpleName.contains("Declaration"))
            && textoElemento.contains("int ") && textoElemento.contains("\"")) {
            holder.newAnnotation(HighlightSeverity.ERROR, "¡Llamando a la IA!...")
                .range(element.textRange)
                .create()
            return
        }
        // -----------------------------------------------------------------------------

        // 🔥 AQUÍ UTILITZEM LA CLASSE PARE DELS ERRORS (PsiErrorElement) 🔥
        // Si no és un error de la classe pare, ignorem la línia i marxem
        if (element !is PsiErrorElement) return

        // Si arribem aquí, ÉS UN ERROR SEGUR. Agafem el missatge original de l'IntelliJ
        val errorOriginal = element.errorDescription
        val ajustes = PluginSettings.getInstance()
        var esErrorDeLlave = false

        // Separem: Els teus 4 personalitzats vs Tota la resta (IA)
        val mensajeAmable = when {
            errorOriginal.contains(";") -> ajustes.mensajePuntoYComa
            errorOriginal.contains(")") -> ajustes.mensajeParentesisCerrar
            errorOriginal.contains("}") -> {
                esErrorDeLlave = true
                ajustes.mensajeCorxeteCerrar
            }
            errorOriginal.contains("]") -> ajustes.mensajeClaudatorCerrar

            // LA RESTA D'ERRORS DE LA CLASSE PARE: Criden a la IA directament
            else -> {
                println("in error else")
                val client = Client.builder()
                    .apiKey("AIzaSyCujA2q9rC-JwWKwla0EofbYBJkKEB-pQM")
                    .build()
                val response = client.models.generateContent(
                    "gemma-4-26b-a4b-it",
                    "I need help",
                    null
                )

                println(response.text())
            }
        }

        // Finalment, ho pintem a la pantalla (en vermell)
        if (esErrorDeLlave && element.textLength == 0) {
            holder.newAnnotation(HighlightSeverity.ERROR, mensajeAmable as String)
                .range(element.textRange)
                .afterEndOfLine()
                .create()
        } else {
            holder.newAnnotation(HighlightSeverity.ERROR, mensajeAmable as String)
                .range(element.textRange)
                .create()
        }
    }
}