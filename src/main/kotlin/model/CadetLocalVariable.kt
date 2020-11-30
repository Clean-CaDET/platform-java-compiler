package model

import model.interfaces.CadetVariable

class CadetLocalVariable(
    val name: String,
    val type: String
) : CadetVariable {

    override fun type(): String = type
}