package hierarchy

import model.CadetClass

class ClassGraphNode(val cadetClass: CadetClass) {
    var parent: ClassGraphNode? = null

    val interfaces = mutableListOf<String>()
}
