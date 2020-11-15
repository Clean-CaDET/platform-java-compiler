package model

class CadetField {
    lateinit var name: String
    lateinit var parent: CadetClass
    lateinit var type: String
    val modifiers = mutableListOf<CadetModifier>()

    fun isStatic(): Boolean {
        modifiers.find { it.type == "static" } ?: return false
        return true
    }
}