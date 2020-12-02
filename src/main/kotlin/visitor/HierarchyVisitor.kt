package visitor

import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.Node
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.type.ClassOrInterfaceType
import model.CadetClass
import parser.node.ClassDeclarationParser
import resolver.SymbolContextMap

class HierarchyVisitor(private val symbolContextMap: SymbolContextMap) {

    fun parseTree(compilationUnit: CompilationUnit, cadetClass: CadetClass) {
        symbolContextMap.createClassContext(cadetClass)
        traverseChildren(
            compilationUnit,
            function = {
                extractHierarchySymbols(it)
                    .forEach { symbol ->
                        symbolContextMap.modifyCurrentClassHierarchy(symbol.nameAsString)
                    }
            }
        )
    }

    private fun traverseChildren(node: Node, function: (node: ClassOrInterfaceDeclaration) -> Unit) {
        if (node is ClassOrInterfaceDeclaration)
            function(node)
        node.childNodes.forEach { traverseChildren(it, function) }
    }

    private fun extractHierarchySymbols(classDeclaration: ClassOrInterfaceDeclaration)
            : List<ClassOrInterfaceType>
            = ClassDeclarationParser.getExtendingClassesAndInterfaces(classDeclaration)
}