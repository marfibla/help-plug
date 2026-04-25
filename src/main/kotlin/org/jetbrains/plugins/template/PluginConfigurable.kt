package org.jetbrains.plugins.template

import com.intellij.openapi.options.Configurable
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel

class PluginConfigurable : Configurable {

    // Creamos dos cajas de texto visuales
    private val cajaPuntoYComa = JBTextField()
    private val cajaParentesis = JBTextField()

    // Este es el nombre que saldrá en el menú de la izquierda en los Settings
    override fun getDisplayName(): String = "Mensajes Amables"

    // Aquí dibujamos el formulario
    override fun createComponent(): JComponent {
        return FormBuilder.createFormBuilder()
            .addLabeledComponent("Mensaje si falta punto y coma (;): ", cajaPuntoYComa)
            .addLabeledComponent("Mensaje si falta paréntesis (): ", cajaParentesis)
            .addComponentFillVertically(JPanel(), 0) // Empuja todo hacia arriba
            .panel
    }

    // Comprueba si el usuario ha escrito algo nuevo para activar el botón "Aplicar"
    override fun isModified(): Boolean {
        val ajustes = PluginSettings.getInstance()
        return cajaPuntoYComa.text != ajustes.mensajePuntoYComa ||
                cajaParentesis.text != ajustes.mensajeParentesis
    }

    // Guarda los textos cuando el usuario pulsa "OK" o "Aplicar"
    override fun apply() {
        val ajustes = PluginSettings.getInstance()
        ajustes.mensajePuntoYComa = cajaPuntoYComa.text
        ajustes.mensajeParentesis = cajaParentesis.text
    }

    // Carga los textos al abrir la pantalla
    override fun reset() {
        val ajustes = PluginSettings.getInstance()
        cajaPuntoYComa.text = ajustes.mensajePuntoYComa
        cajaParentesis.text = ajustes.mensajeParentesis
    }
}