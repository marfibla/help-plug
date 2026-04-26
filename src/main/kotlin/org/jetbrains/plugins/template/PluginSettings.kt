package org.jetbrains.plugins.template

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.*
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "MensajesAmablesSettings",
    storages = [Storage("MensajesAmables.xml")]
)
class PluginSettings : PersistentStateComponent<PluginSettings> {

    // Mensajes por defecto que saldrán la primera vez
    var mensajePuntoYComa: String = "Te has dejado un punto y coma :p"
    var mensajeParentesisCerrar: String = "Te comiste un paréntesis de cierre ')' :("
    var mensajeCorxeteCerrar: String = "Te comiste un corchete de cierre '}' :/"
    var mensajeClaudatorCerrar: String = "Te comiste un claudator de cierre ']'._."


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