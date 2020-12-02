package visitor

import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.Node
import com.github.javaparser.ast.stmt.BlockStmt
import com.github.javaparser.ast.type.ClassOrInterfaceType
import model.CadetClass
import parser.node.ClassDeclarationParser
import resolver.SymbolContextMap

class HierarchyVisitor(private val symbolContextMap: SymbolContextMap) {

    fun parseTree(compilationUnit: CompilationUnit, cadetClass: CadetClass) {
        symbolContextMap.createClassContext(cadetClass)

        ClassDeclarationParser.getExtendingClassesAndInterfaces(compilationUnit)
            .forEach { node ->
                symbolContextMap.modifyCurrentClassHierarchy(node.nameAsString)
            }
    }
}