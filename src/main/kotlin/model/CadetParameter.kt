package model

import model.abs.CadetVariable

class CadetParameter (
    val name: String,
    type: String
) : CadetVariable() {

    init {
        this.type = type
    }
}