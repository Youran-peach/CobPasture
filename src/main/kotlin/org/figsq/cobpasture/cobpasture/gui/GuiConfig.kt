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
        PARTY_SELECT_GUI_TITLE = config.getString("party_select_gui.title")!!
        PARTY_SELECT_GUI_ELEMENTS_NAME = config.getString("party_select_gui.elements.name")!!
        PARTY_SELECT_GUI_ELEMENTS_LORE = config.getStringList("party_select_gui.elements.lore")!!
        PASTURE_GUI_TITLE = config.getString("pasture_gui.title")!!
        PASTURE_GUI_PARENT1_NAME = config.getString("pasture_gui.parent1.name")!!
        PASTURE_GUI_PARENT1_LORE = config.getStringList("pasture_gui.parent1.lore")!!
        PASTURE_GUI_PARENT2_NAME = config.getString("pasture_gui.parent2.name")!!
        PASTURE_GUI_PARENT2_LORE = config.getStringList("pasture_gui.parent2.lore")!!
        PASTURE_GUI_EGG_NAME = config.getString("pasture_gui.egg.name")!!
        PASTURE_GUI_EGG_LORE = config.getStringList("pasture_gui.egg.lore")!!
        PASTURE_LIST_GUI_TITLE = config.getString("pasture_list_gui.title")!!
        PASTURE_LIST_GUI_ELEMENTS_NAME = config.getString("pasture_list_gui.elements.name")!!
        PASTURE_LIST_GUI_ELEMENTS_LORE = config.getStringList("pasture_list_gui.elements.lore")!!
    }
}