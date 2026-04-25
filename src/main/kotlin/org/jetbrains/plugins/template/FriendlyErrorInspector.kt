package org.jetbrains.plugins.template

import com.intellij.lang.annotation.*
import com.intellij.psi.*

class FriendlyAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {

        // Comprovem si l'element que estem llegint és un error de codi
        if (element is PsiErrorElement) {

            // Agafem l'error original de l'IntelliJ en minúscules per buscar més fàcilment
            val descripcioOriginal = element.errorDescription.lowercase()

            // Triem el teu missatge personalitzat segons el que digui l'error
            val elTeuMissatgeDivertit: String? = when {
                //descripcioOriginal.contains("literal") || descripcioOriginal.contains("unclosed") -> "Tanca les cometes, que s'escapen les paraules!"
                descripcioOriginal.contains("empty character") -> "Has creat un forat negre! Un 'char' ha de tenir alguna lletra a dins"
                descripcioOriginal.contains("'{'") -> "Obre la clau '{', que si no el compilador no pot entrar!"
                descripcioOriginal.contains("'}'") -> "M'has deixat la porta oberta! Falta tancar una clau '}'"
                descripcioOriginal.contains("'['") -> "Obre el claudàtor '[', que et perds pels arrays!"
                descripcioOriginal.contains("']'") -> "M'has deixat la capsa oberta! Falta tancar el claudàtor ']'"
                descripcioOriginal.contains("','") -> "Et falta una coma per separar això bé!"
                descripcioOriginal.contains("illegal start") -> "Falta alguna cosa aquí al mig..."
                descripcioOriginal.contains(";") && descripcioOriginal.contains("expected") -> "¡No seas tonto, te has dejado un punto y coma!"
                else -> null
            }
            // Si hem trobat un missatge dels nostres, substituïm l'avís
            if (elTeuMissatgeDivertit != null) {
                holder.newAnnotation(HighlightSeverity.ERROR, elTeuMissatgeDivertit)
                    .range(element.textRange)
                    .create()
            }
        }
    }
}