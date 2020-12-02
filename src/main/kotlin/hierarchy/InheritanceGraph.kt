package hierarchy

import model.CadetClass
import model.CadetMember
import model.CadetParameter

class InheritanceGraph {

    private val classGraph = mutableMapOf<String, ClassGraphNode>()
    private val baseKey = "Object"

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
        return classList
    }

    fun getClassParent(className: String): CadetClass? = classGraph[className]?.parent?.cadetClass

    private fun recursiveParentAdd(node: ClassGraphNode, list: MutableCollection<CadetClass>) {
        list.add(node.cadetClass)
        if (node.parent != null)
            recursiveParentAdd(node.parent!!, list)
    }

    fun getClass(name: String): CadetClass? = classGraph[name]?.cadetClass

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
        val equals = CadetMember().apply {
            name = "equals"
            returnType = "boolean"
            params.add(CadetParameter("object", "Object"))
            parent = obj
        }
        val constructor = CadetMember().apply {
            name = "Object"
            returnType = "Object"
            parent = obj
        }
        obj.members.apply {
            add(toString); add(hashCode); add(clone); add(equals); add(constructor);
        }
        return obj
    }

    fun printHierarchy() {
        classGraph.forEach { (_, c) ->
            printClassHierarchy(c)
            println()
        }
    }

    private fun printClassHierarchy(c: ClassGraphNode) {
        print("\t-> ${c.cadetClass.name}")
        c.parent?.let {
            printClassHierarchy(c.parent!!)
        }
    }
}