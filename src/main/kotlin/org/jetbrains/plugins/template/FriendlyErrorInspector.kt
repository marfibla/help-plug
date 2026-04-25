package org.jetbrains.plugins.template

import com.intellij.lang.annotation.*
import com.intellij.psi.*

class FriendlyAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {

        // Comprobamos si el elemento que estamos leyendo es un error de código rojo
        if (element is PsiErrorElement) {

            // Si el error original de IntelliJ se queja del punto y coma...
            if (element.errorDescription.contains(";")) {

                // ¡Lanzamos tu mensaje encima!
                holder.newAnnotation(HighlightSeverity.ERROR, "¡No seas tonto, te has dejado un punto y coma! 🙄")
                    .range(element.textRange)
                    .create()
            }
        }
    }
}