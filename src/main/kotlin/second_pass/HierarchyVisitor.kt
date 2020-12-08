package second_pass

import cadet_model.CadetClass
import com.github.javaparser.ast.CompilationUnit
import first_pass.node_parser.ClassDeclarationParser
import hierarchy.HierarchyGraph
import second_pass.context.VisitorContext

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