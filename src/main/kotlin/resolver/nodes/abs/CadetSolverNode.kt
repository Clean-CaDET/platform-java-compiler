package resolver.nodes.abs

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.expr.MethodCallExpr
import com.github.javaparser.ast.expr.NameExpr
import com.github.javaparser.ast.expr.ObjectCreationExpr
import resolver.SymbolContextMap

abstract class CadetSolverNode<T>(node: Node, symbolMap: SymbolContextMap) : ReferenceSolverNode(node, symbolMap) {

    protected var resolvedReference: T? = null
    protected abstract fun doResolve()

    private fun notifyContextOfUsage() {
        resolvedReference ?: return
        symbolMap.notifyUsage(resolvedReference)
    }

    override fun resolve() {
        doResolve()
        notifyContextOfUsage()

        // Diagnostics for failed resolving
        resolvedReference ?:
            println("${node.metaModel.typeName} : '${nodeName(node)}' from ${symbolMap.getContextClassName()}")
    }

    // TODO Diagnostics method
    private fun nodeName(node: Node): String {
        return when(node) {
            is MethodCallExpr -> "${node.nameAsString}()"
            is ObjectCreationExpr -> "new ${node.typeAsString}()"
            is NameExpr -> node.nameAsString
            else -> "Name unknown"
        }
    }
}