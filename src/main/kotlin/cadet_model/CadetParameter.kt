package cadet_model

import cadet_model.abs.CadetVariable

class CadetParameter(
    val name: String,
    type: String
) : CadetVariable() {

    init {
        this.type = type
    }
}