package model

import model.interfaces.CadetVariable

class CadetField : CadetVariable {
    lateinit var name: String
    lateinit var parent: CadetClass
    lateinit var type: String
    val modifiers = mutableListOf<CadetModifier>()

    fun isStatic(): Boolean {
        modifiers.find { it.type == "static" } ?: return false
        return true
    }

    override fun type(): String = type
}