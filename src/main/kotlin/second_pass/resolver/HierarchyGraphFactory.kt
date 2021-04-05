package second_pass.resolver

import prototype_dto.ClassPrototype
import prototype_dto.InterfacePrototype
import prototype_dto.JavaPrototype
import second_pass.hierarchy.HierarchyGraph

object HierarchyGraphFactory {

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