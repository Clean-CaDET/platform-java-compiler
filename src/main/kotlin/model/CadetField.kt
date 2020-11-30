package model

import model.abs.CadetVariable

class CadetField : CadetVariable() {
    lateinit var name: String
    lateinit var parent: CadetClass
    val modifiers = mutableListOf<CadetModifier>()

    fun isStatic(): Boolean {
        modifiers.find { it.type == "static" } ?: return false
        return true
    }
}