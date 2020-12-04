package visitor

import com.github.javaparser.ast.CompilationUnit
import context.VisitorContext
import hierarchy.HierarchyGraph
import model.CadetClass
import parser.node.ClassDeclarationParser

class HierarchyVisitor(private val visitorContext: VisitorContext, private val hierarchyGraph: HierarchyGraph) {

    fun parseTree(compilationUnit: CompilationUnit, cadetClass: CadetClass) {
        visitorContext.createClassContext(cadetClass)

        ClassDeclarationParser.getExtendingClassesAndInterfaces(compilationUnit)
            .forEach { node ->
                hierarchyGraph.modifyClassHierarchy(visitorContext.getCurrentClassName(), node.nameAsString)
            }
    }
}