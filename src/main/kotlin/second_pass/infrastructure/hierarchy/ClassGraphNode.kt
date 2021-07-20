package second_pass.infrastructure.hierarchy

import cadet_model.CadetClass

class ClassGraphNode(val cadetClass: CadetClass) {
    var parent: ClassGraphNode? = null

    val interfaces = mutableListOf<String>()
}
