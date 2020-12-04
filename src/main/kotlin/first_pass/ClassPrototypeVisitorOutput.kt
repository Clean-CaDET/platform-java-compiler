package first_pass

import cadet_model.CadetClass

class ClassPrototypeVisitorOutput {
    lateinit var cadetClass: CadetClass
    val interfaces = mutableListOf<String>()
}