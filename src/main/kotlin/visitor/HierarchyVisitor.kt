package visitor

import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.Node
import com.github.javaparser.ast.type.ClassOrInterfaceType
import model.CadetClass
import resolver.SymbolContextMap

class HierarchyVisitor(private val symbolContextMap: SymbolContextMap) {

    fun parseTree(compilationUnit: CompilationUnit, cadetClass: CadetClass) {
        symbolContextMap.createClassContext(cadetClass)
        traverseChildren(
            compilationUnit,
            onSymbolFound = {
                if (it != symbolContextMap.getContextClassName())
                    symbolContextMap.modifyCurrentClassHierarchy(it)
            }
        )
    }

    private fun traverseChildren(
        node: Node,
        onSymbolFound: (symbolName: String) -> Unit
    ){
        if (node is ClassOrInterfaceType) {
            onSymbolFound(node.nameAsString)
        }
        node.childNodes.forEach { traverseChildren(it, onSymbolFound) }
    }
}