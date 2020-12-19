package cadet_model

import cadet_model.abs.CadetVariable

class CadetField : CadetVariable() {
    lateinit var name: String
    lateinit var parent: CadetClass
    val modifiers = mutableListOf<CadetModifier>()
}