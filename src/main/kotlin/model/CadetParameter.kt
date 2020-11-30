package model

import model.interfaces.CadetVariable

class CadetParameter(
    val name: String,
    val type: String
) : CadetVariable {

    override fun type(): String = type
}