package second_pass.hierarchy

import cadet_model.CadetClass
import prototype_dto.ClassPrototype
import prototype_dto.InterfacePrototype
import prototype_dto.JavaPrototype

class HierarchyGraph {

    private val classGraph = mutableMapOf<String, ClassGraphNode>()
    private val interfaceMap = mutableMapOf<String, String>()

    object Factory {
        fun initializeHierarchyGraph(prototypes: List<JavaPrototype>): HierarchyGraph {
            val hierarchyGraph = HierarchyGraph()

            loadHierarchyNodes(prototypes, hierarchyGraph)
            connectHierarchyNodes(prototypes, hierarchyGraph)

            return hierarchyGraph
        }

        private fun loadHierarchyNodes(prototypes: List<JavaPrototype>, hierarchyGraph: HierarchyGraph) {
            for (prototype in prototypes) {
                if (prototype is InterfacePrototype) hierarchyGraph.addInterface(prototype.getName())
                else if (prototype is ClassPrototype) hierarchyGraph.addClass(prototype.cadetClass)
            }
        }

        private fun connectHierarchyNodes(prototypes: List<JavaPrototype>, hierarchyGraph: HierarchyGraph) {
            for (prototype in prototypes) {
                if (prototype is ClassPrototype) {
                    for (symbol in prototype.hierarchySymbols) {
                        hierarchyGraph.modifyClassHierarchy(prototype.getName(), symbol)
                    }
                }
            }
        }
    }

    fun getClass(name: String): CadetClass? = classGraph[name]?.cadetClass
    fun getAllClasses(): List<CadetClass> {
        val classes = mutableListOf<CadetClass>()
        classGraph.forEach{(_,v) -> classes.add(v.cadetClass)}
        return classes
    }
    fun getClassParent(className: String): CadetClass? = classGraph[className]?.parent?.cadetClass

    fun addClass(cadetClass: CadetClass) {
        val node = ClassGraphNode(cadetClass)
        node.parent = null
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
        node ?: throw IllegalArgumentException("Class $className not found in second_pass.infrastructure.hierarchy")
        node.interfaces.add(interfaceName)
    }

    private fun addSuperClass(className: String, parentName: String) {
        val node = classGraph[className]
        val parent = classGraph[parentName]

        node ?: throw IllegalArgumentException("Class $className not found in second_pass.infrastructure.hierarchy")
        parent ?: throw IllegalArgumentException("Parent class $parentName not found in second_pass.infrastructure.hierarchy")

        node.parent = parent
        node.cadetClass.parent = parent.cadetClass
    }

    fun isSuperType(className: String, parentName: String): Boolean {
        var node = classGraph[className]?.parent
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
        node ?: return mutableListOf()
        // TODO throw IllegalArgumentException("Class $className not found in second_pass.infrastructure.hierarchy.")

        while (node != null) {
            hierarchy.add(node.cadetClass)
            node = node.parent
        }

        return hierarchy
    }
}