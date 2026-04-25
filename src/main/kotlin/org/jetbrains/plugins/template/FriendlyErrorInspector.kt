package org.jetbrains.plugins.template

import com.intellij.lang.annotation.*
import com.intellij.psi.*

class FriendlyAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {

        if (element !is PsiErrorElement) return

        val errorOriginal = element.errorDescription

        // 🕵️ EL CHIVATO: Por si el corchete se llama distinto en esta versión
        println("EL ERROR REAL ES: -> $errorOriginal <-")

        var mensajeAmable = ""
        var esErrorDeLlave = false

        when {
            errorOriginal.contains(";") -> {
                mensajeAmable = "¡No seas tonto, te has dejado un punto y coma! 🙄"
            }
            errorOriginal.contains(")") -> {
                mensajeAmable = "Te comiste un paréntesis de cierre ')'. ¡Abre y cierra! 😋"
            }
            errorOriginal.contains("}") -> {
                mensajeAmable = "¡Se te escapa el código! Te falta cerrar una llave '}' por aquí. 🚪"
                esErrorDeLlave = true // <-- ¡Marcamos que es la llave!
            }
            errorOriginal.contains("]") -> {
                mensajeAmable = "Te dejaste un corchete ']' abierto. El array se está vaciando. 📦"
            }
        }

        if (mensajeAmable.isNotEmpty()) {

            // Solo usamos la magia de empujar al final si es la LLAVE y mide 0
            if (esErrorDeLlave && element.textLength == 0) {
                holder.newAnnotation(HighlightSeverity.ERROR, mensajeAmable)
                    .range(element.textRange)
                    .afterEndOfLine()
                    .create()
            } else {
                // El corchete y los demás se pintan de forma normal en su sitio
                holder.newAnnotation(HighlightSeverity.ERROR, mensajeAmable)
                    .range(element.textRange)
                    .create()
            }
        }
    }
}