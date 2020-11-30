package hierarchy

import model.CadetClass
import model.CadetMember

class InheritanceGraph {

    private val classGraph = mutableMapOf<String, ClassGraphNode>()
    private val baseKey = "base"

    constructor(baseClass: CadetClass) {
        classGraph[baseKey] = ClassGraphNode(baseClass)
    }
    constructor() {
        classGraph[baseKey] = ClassGraphNode(instantiateObjectBase())
    }

    fun addClass(cadetClass: CadetClass) {
        ClassGraphNode(cadetClass)
            .also {
                it.parent = baseClass()
                classGraph[cadetClass.name] = it
            }
    }

    private fun baseClass() = classGraph[baseKey]

    fun modifyClassParent(className: String, parentName: String) {
        classGraph[className]?.parent = classGraph[parentName]
    }

    fun getClassHierarchy(className: String): List<CadetClass> {
        val classList = mutableListOf<CadetClass>()
        classGraph[className]
            ?.let {
                recursiveParentAdd(it, classList)
            }
        return classList.reversed()
    }

    fun getClassParent(className: String): CadetClass? = classGraph[className]?.parent?.cadetClass

    private fun recursiveParentAdd(node: ClassGraphNode, list: MutableCollection<CadetClass>) {
        if (node.parent != null)
            recursiveParentAdd(node.parent!!, list)
        list.add(node.cadetClass)
    }

    private fun instantiateObjectBase(): CadetClass {
        val obj = CadetClass()
        obj.name = "Object"

        val toString = CadetMember().apply {
            name = "toString"
            returnType = "String"
            parent = obj
        }
        val hashCode = CadetMember().apply {
            name = "hashCode"
            returnType = "int"
            parent = obj
        }
        val clone = CadetMember().apply {
            name = "clone"
            returnType = "Object"
            parent = obj
        }
        // TODO Equals with a wildcard or something similar
        obj.members.apply {
            add(toString); add(hashCode); add(clone);
        }

        return obj
    }
}