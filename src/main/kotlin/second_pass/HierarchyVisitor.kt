package second_pass

import com.github.javaparser.ast.CompilationUnit
import second_pass.context.VisitorContext
import cadet_model.CadetClass
import first_pass.node_parser.ClassDeclarationParser
import second_pass.hierarchy.HierarchyGraph

class HierarchyVisitor(
    private val visitorContext: VisitorContext,
    private val hierarchyGraph: HierarchyGraph
) {
    fun parseTree(compilationUnit: CompilationUnit, cadetClass: CadetClass) {
        visitorContext.createClassContext(cadetClass)

        ClassDeclarationParser.getExtendingClassesAndInterfaces(compilationUnit)
            .forEach { node ->
                hierarchyGraph.modifyClassHierarchy(visitorContext.getCurrentClassName(), node.nameAsString)
            }
    }
}