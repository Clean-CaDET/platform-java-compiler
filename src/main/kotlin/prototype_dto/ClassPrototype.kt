package prototype_dto

import cadet_model.CadetClass

class ClassPrototype(val cadetClass: CadetClass) : JavaPrototype {
    val hierarchySymbols = mutableListOf<String>()

    override fun getName(): String {
        return cadetClass.name
    }
}