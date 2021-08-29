package cadet_model

import cadet_model.abs.CadetVariable

class CadetStaticClassName(type: String) : CadetVariable() {
    init {
        this.type = type
    }
}