package hierarchy

import model.CadetClass
import model.CadetMember
import model.CadetParameter
import java.lang.IllegalArgumentException

class HierarchyGraph {

    private val classGraph = mutableMapOf<String, ClassGraphNode>()
    private val interfaceMap = mutableMapOf<String, String>()
    private val baseKey = "Object"

    init {
        classGraph[baseKey] = ClassGraphNode(instantiateObjectBase())
    }

    fun getClass(name: String): CadetClass? = classGraph[name]?.cadetClass
    fun getClassParent(className: String): CadetClass? = classGraph[className]?.parent?.cadetClass
    fun getClasses(): List<CadetClass> {
        return mutableListOf<CadetClass>().apply {
            classGraph.forEach { classNode ->
                this.add(classNode.value.cadetClass)
            }
        }
    }

    fun addClass(cadetClass: CadetClass) {
        ClassGraphNode(cadetClass)
            .let {
                it.parent = baseClass()
                classGraph[cadetClass.name] = it
            }
    }

    fun addInterface(name: String) {
        interfaceMap[name] = name
    }

    fun modifyClassHierarchy(className: String, symbolName: String) {
        if (classGraph[className] != null) {
            classGraph[symbolName]?.let {
                addSuperClass(className, symbolName)
                return
            }
            interfaceMap[symbolName]?.let {
                addInterfaceImplementation(className, symbolName)
            }
        }
    }

    private fun addInterfaceImplementation(className: String, interfaceName: String) {
        interfaceName.let {
            interfaceMap[it] = it
            classGraph[className]!!.interfaces.add(it)
        }
    }

    private fun addSuperClass(className: String, parentName: String) {
        classGraph[className].let { classNode ->
            classNode ?: throw IllegalArgumentException("Class $className not found in hierarchy")
            classGraph[parentName].let { parentNode ->
                parentNode ?: throw IllegalArgumentException("Parent class $parentName not found in hierarchy")
                classNode.parent = parentNode
                classNode.cadetClass.parent = parentNode.cadetClass
            }
        }
    }

    private fun baseClass() = classGraph[baseKey]

    fun getClassHierarchy(className: String): List<CadetClass> {
        classGraph[className]?.let { node ->
            return mutableListOf<CadetClass>().also { list ->
                recursiveParentAdd(node, list)
            }
        }
        throw IllegalArgumentException("Class $className not found in hierarchy.")
    }

    private fun recursiveParentAdd(node: ClassGraphNode, list: MutableCollection<CadetClass>) {
        list.add(node.cadetClass)
        node.parent?.let { recursiveParentAdd(it, list) }
    }

    fun isSuperType(className: String, parentName: String): Boolean {
        classGraph[className]?.let { classNode ->
            if (classNode.cadetClass.name == parentName) return true
            classNode.parent?.let { parentNode ->
                return isSuperType(parentNode.cadetClass.name, parentName)
            }
        }
        return false
    }

    fun isImplementation(className: String, interfaceName: String): Boolean {
        classGraph[className]?.let { classNode ->
            if (classNode.interfaces.contains(interfaceName)) return true
            classNode.parent?.let { parentNode ->
                return isImplementation(parentNode.cadetClass.name, interfaceName)
            }
        }
        return false
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
}