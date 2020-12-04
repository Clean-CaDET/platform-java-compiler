package first_pass

import cadet_model.CadetClass

class OuterVisitorOutput {
    lateinit var cadetClass: CadetClass
    val interfaces = mutableListOf<String>()
}