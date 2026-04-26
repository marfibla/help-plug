package org.jetbrains.plugins.template

import com.intellij.openapi.options.Configurable
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel

class PluginConfigurable : Configurable {

    // Creamos una caja de texto visual para cada variable
    private val cajaPuntoYComa = JBTextField()
    private val cajaParentesisCerrar = JBTextField()
    private val cajaCorxeteCerrar = JBTextField()
    private val cajaClaudatorCerrar = JBTextField()

    override fun getDisplayName(): String = "Mensajes Amables"

    // Dibujo de las cajas
    override fun createComponent(): JComponent {
        return FormBuilder.createFormBuilder()
            .addLabeledComponent("Mensaje si falta punto y coma (;): ", cajaPuntoYComa)
            .addLabeledComponent("Mensaje si falta paréntesis ')': ", cajaParentesisCerrar)
            .addLabeledComponent("Mensaje si falta corchete '}': ", cajaCorxeteCerrar)
            .addLabeledComponent("Mensaje si falta claudátor ']': ", cajaClaudatorCerrar)
            .addComponentFillVertically(JPanel(), 0) // Empeny tot cap amunt
            .panel
    }

    // 3. Comprova si l'usuari ha modificat ALGUNA de les caixes
    override fun isModified(): Boolean {
        val ajustes = PluginSettings.getInstance()
        return cajaPuntoYComa.text != ajustes.mensajePuntoYComa ||
                cajaParentesisCerrar.text != ajustes.mensajeParentesisCerrar ||
                cajaCorxeteCerrar.text != ajustes.mensajeCorxeteCerrar ||
                cajaClaudatorCerrar.text != ajustes.mensajeClaudatorCerrar
    }

    // Guarda los textos nuevos
    override fun apply() {
        val ajustes = PluginSettings.getInstance()
        ajustes.mensajePuntoYComa = cajaPuntoYComa.text
        ajustes.mensajeParentesisCerrar = cajaParentesisCerrar.text
        ajustes.mensajeCorxeteCerrar = cajaCorxeteCerrar.text
        ajustes.mensajeClaudatorCerrar = cajaClaudatorCerrar.text
    }

    // Carga los textos al abrir la pantalla de settings
    override fun reset() {
        val ajustes = PluginSettings.getInstance()
        cajaPuntoYComa.text = ajustes.mensajePuntoYComa
        cajaParentesisCerrar.text = ajustes.mensajeParentesisCerrar
        cajaCorxeteCerrar.text = ajustes.mensajeCorxeteCerrar
        cajaClaudatorCerrar.text = ajustes.mensajeClaudatorCerrar
    }
}