package org.jetbrains.plugins.template

import com.intellij.openapi.options.Configurable
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel

class PluginConfigurable : Configurable {

    // 1. Creem una caixa de text visual per a cadascuna de les teves 4 variables
    private val cajaPuntoYComa = JBTextField()
    private val cajaParentesisCerrar = JBTextField()
    private val cajaCorxeteCerrar = JBTextField()
    private val cajaClaudatorCerrar = JBTextField()

    // Aquest és el nom que sortirà al menú de l'esquerra als Settings
    override fun getDisplayName(): String = "Mensajes Amables"

    // 2. Aquí dibuixem el formulari amb TOTES les caixes
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

    // 4. Guarda TOTS els textos quan l'usuari prem "OK" o "Aplicar"
    override fun apply() {
        val ajustes = PluginSettings.getInstance()
        ajustes.mensajePuntoYComa = cajaPuntoYComa.text
        ajustes.mensajeParentesisCerrar = cajaParentesisCerrar.text
        ajustes.mensajeCorxeteCerrar = cajaCorxeteCerrar.text
        ajustes.mensajeClaudatorCerrar = cajaClaudatorCerrar.text
    }

    // 5. Carrega TOTS els textos en obrir la pantalla de Settings
    override fun reset() {
        val ajustes = PluginSettings.getInstance()
        cajaPuntoYComa.text = ajustes.mensajePuntoYComa
        cajaParentesisCerrar.text = ajustes.mensajeParentesisCerrar
        cajaCorxeteCerrar.text = ajustes.mensajeCorxeteCerrar
        cajaClaudatorCerrar.text = ajustes.mensajeClaudatorCerrar
    }
}