package org.figsq.cobpasture.cobpasture.gui

import org.figsq.cobpasture.cobpasture.CobPasture

object GuiConfig {
    lateinit var PARTY_SELECT_GUI_TITLE: String
    lateinit var PARTY_SELECT_GUI_ELEMENTS_NAME: String
    lateinit var PARTY_SELECT_GUI_ELEMENTS_LORE: List<String>

    lateinit var PASTURE_GUI_TITLE: String
    lateinit var PASTURE_GUI_PARENT1_NAME: String
    lateinit var PASTURE_GUI_PARENT1_LORE: List<String>
    lateinit var PASTURE_GUI_PARENT2_NAME: String
    lateinit var PASTURE_GUI_PARENT2_LORE: List<String>
    lateinit var PASTURE_GUI_EGG_NAME: String
    lateinit var PASTURE_GUI_EGG_LORE: List<String>

    lateinit var PASTURE_LIST_GUI_TITLE: String
    lateinit var PASTURE_LIST_GUI_ELEMENTS_NAME: String
    lateinit var PASTURE_LIST_GUI_ELEMENTS_LORE: List<String>


    fun load() {
        val config = CobPasture.INSTANCE.config
        val section = config.getConfigurationSection("gui")!!
        PARTY_SELECT_GUI_TITLE = section.getString("party-select-gui.title")!!
        PARTY_SELECT_GUI_ELEMENTS_NAME = section.getString("party-select-gui.elements.name")!!
        PARTY_SELECT_GUI_ELEMENTS_LORE = section.getStringList("party-select-gui.elements.lore")
        PASTURE_GUI_TITLE = section.getString("pasture-gui.title")!!
        PASTURE_GUI_PARENT1_NAME = section.getString("pasture-gui.parent1.name")!!
        PASTURE_GUI_PARENT1_LORE = section.getStringList("pasture-gui.parent1.lore")
        PASTURE_GUI_PARENT2_NAME = section.getString("pasture-gui.parent2.name")!!
        PASTURE_GUI_PARENT2_LORE = section.getStringList("pasture-gui.parent2.lore")
        PASTURE_GUI_EGG_NAME = section.getString("pasture-gui.egg.name")!!
        PASTURE_GUI_EGG_LORE = section.getStringList("pasture-gui.egg.lore")
        PASTURE_LIST_GUI_TITLE = section.getString("pasture-list-gui.title")!!
        PASTURE_LIST_GUI_ELEMENTS_NAME = section.getString("pasture-list-gui.elements.name")!!
        PASTURE_LIST_GUI_ELEMENTS_LORE = section.getStringList("pasture-list-gui.elements.lore")
    }
}