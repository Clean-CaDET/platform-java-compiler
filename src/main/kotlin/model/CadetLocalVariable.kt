package model

import model.abs.CadetVariable

class CadetLocalVariable(
    val name: String,
    type: String
) : CadetVariable() {

    init {
        this.type = type
    }
}