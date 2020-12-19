package second_pass.context

import cadet_model.CadetClass
import cadet_model.CadetField

open class ClassContext(val cadetClass: CadetClass) {

    fun getField(name: String): CadetField? {
        return cadetClass.fields.find { it.name == name }
    }
}