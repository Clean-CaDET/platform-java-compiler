package hierarchy

import cadet_model.CadetClass
import cadet_model.CadetMember
import cadet_model.CadetMemberType
import cadet_model.CadetParameter

class HierarchyGraph {

    private val classGraph = mutableMapOf<String, ClassGraphNode>()
    private val interfaceMap = mutableMapOf<String, String>()
    private val baseClass: ClassGraphNode

    init {
        val baseKey = "Object"
        ClassGraphNode(instantiateObjectBase()).let {
            classGraph[baseKey] = it
            baseClass = it
        }
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
                it.parent = baseClass
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
                parentNode
                    ?: throw IllegalArgumentException("Parent class $parentName not found in hierarchy")
                classNode.parent = parentNode
                classNode.cadetClass.parent = parentNode.cadetClass
            }
        }
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

    private fun instantiateObjectBase(): CadetClass {
        val obj = CadetClass()
        obj.name = "Object"
        obj.fullName = "java.lang.Object"

        val toString = CadetMember().apply {
            name = "toString"
            returnType = "String"
            parent = obj
            cadetMemberType = CadetMemberType.Method
        }
        val hashCode = CadetMember().apply {
            name = "hashCode"
            returnType = "int"
            parent = obj
            cadetMemberType = CadetMemberType.Method
        }
        val clone = CadetMember().apply {
            name = "clone"
            returnType = "Object"
            parent = obj
            cadetMemberType = CadetMemberType.Method
        }
        val equals = CadetMember().apply {
            name = "equals"
            returnType = "boolean"
            params.add(CadetParameter("object", "Object"))
            parent = obj
            cadetMemberType = CadetMemberType.Method
        }
        val constructor = CadetMember().apply {
            name = "Object"
            returnType = "Object"
            parent = obj
            cadetMemberType = CadetMemberType.Method
        }
        obj.members.apply {
            add(toString); add(hashCode); add(clone); add(equals); add(constructor);
        }
        return obj
    }
}