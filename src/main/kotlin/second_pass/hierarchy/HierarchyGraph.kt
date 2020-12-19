package second_pass.hierarchy

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
        val node = ClassGraphNode(cadetClass)
        node.parent = baseClass
        classGraph[cadetClass.name] = node
    }

    fun addInterface(name: String) {
        interfaceMap[name] = name
    }

    fun modifyClassHierarchy(className: String, symbolName: String) {
        if (classGraph[symbolName] != null)
            addSuperClass(className, symbolName)
        else
            addInterfaceImplementation(className, symbolName)
    }

    private fun addInterfaceImplementation(className: String, interfaceName: String) {
        val node = classGraph[className]
        node ?: throw IllegalArgumentException("Class $className not found in second_pass.hierarchy")
        node.interfaces.add(interfaceName)
    }

    private fun addSuperClass(className: String, parentName: String) {
        val node = classGraph[className]
        val parent = classGraph[parentName]

        node ?: throw IllegalArgumentException("Class $className not found in second_pass.hierarchy")
        parent ?: throw IllegalArgumentException("Parent class $parentName not found in second_pass.hierarchy")

        node.parent = parent
        node.cadetClass.parent = parent.cadetClass
    }

    fun isSuperType(className: String, parentName: String): Boolean {
        var node = classGraph[className]?.parent
        node ?: throw java.lang.IllegalArgumentException("Class $className not found. (Supertype lookup)")
        while (node != null) {
            if (node.cadetClass.name == parentName) return true
            node = node.parent
        }
        return false
    }

    fun isImplementation(className: String, interfaceName: String): Boolean {
        var node = classGraph[className]
        while (node != null) {
            if (node.interfaces.contains(interfaceName)) return true
            node = node.parent
        }
        return false
    }

    fun getClassHierarchy(className: String): List<CadetClass> {
        val hierarchy = mutableListOf<CadetClass>()
        var node = classGraph[className]
        node ?: throw IllegalArgumentException("Class $className not found in second_pass.hierarchy.")

        while (node != null) {
            hierarchy.add(node.cadetClass)
            node = node.parent
        }

        return hierarchy
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