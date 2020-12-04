package second_pass.resolver.solver_nodes.abs

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.expr.MethodCallExpr
import com.github.javaparser.ast.expr.NameExpr
import com.github.javaparser.ast.expr.ObjectCreationExpr
import second_pass.resolver.SymbolSolvingBundle

abstract class CadetSolverNode<T>(
    node: Node,
    protected val symbolSolvingBundle: SymbolSolvingBundle
) : BaseSolverNode(node) {

    protected var resolvedReference: T? = null
    protected abstract fun doResolve()

    private fun notifyContextOfUsage() {
        resolvedReference ?: return
        symbolSolvingBundle.getVisitorContext().notifyUsage(resolvedReference)
    }

    override fun resolve() {
        doResolve()
        notifyContextOfUsage()

        // Diagnostics for failed resolving
        resolvedReference ?:
            println("${node.metaModel.typeName} : '${nodeName(node)}' from ${symbolSolvingBundle.getVisitorContext().getCurrentClassName()}")
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