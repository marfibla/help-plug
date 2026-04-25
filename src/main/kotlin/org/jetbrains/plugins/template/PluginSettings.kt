package org.jetbrains.plugins.template

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.*
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "MensajesAmablesSettings",
    storages = [Storage("MensajesAmables.xml")]
)
class PluginSettings : PersistentStateComponent<PluginSettings> {

    // Aquí ponemos los mensajes por defecto que saldrán la primera vez
    var mensajePuntoYComa: String = "¡No seas tonto, te has dejado un punto y coma! 🙄"
    var mensajeParentesis: String = "Te comiste un paréntesis de cierre ')'. ¡Abre y cierra! 😋"

    override fun getState(): PluginSettings = this

    override fun loadState(state: PluginSettings) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        fun getInstance(): PluginSettings {
            return ApplicationManager.getApplication().getService(PluginSettings::class.java)
        }
    }
}