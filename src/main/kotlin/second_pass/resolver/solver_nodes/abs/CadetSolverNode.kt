package second_pass.resolver.solver_nodes.abs

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.expr.FieldAccessExpr
import com.github.javaparser.ast.expr.MethodCallExpr
import com.github.javaparser.ast.expr.NameExpr
import com.github.javaparser.ast.expr.ObjectCreationExpr
import second_pass.resolver.SymbolResolver

abstract class CadetSolverNode<T : Any>(
    node: Node,
    protected val resolver: SymbolResolver
) : BaseSolverNode(node) {

    protected var resolvedReference: T? = null
    protected abstract fun doResolve()

    private fun notifyContextOfUsage() {
        if (resolvedReference == null) {
            try {
                returnType
                return
            }
            catch (e: UninitializedPropertyAccessException) {
                returnType = SymbolResolver.WildcardType
            }
        }
        else
            resolver.getVisitorContext().notifyUsage(resolvedReference!!)
    }

    override fun resolve() {
        doResolve()
        notifyContextOfUsage()

        // Diagnostics for failed resolving
        resolvedReference ?: println(
            "${node.metaModel.typeName} : '${nodeName(node)}' from ${
                resolver.getVisitorContext().getCurrentClassName()
            }"
        )
    }

    // TODO Diagnostics method
    private fun nodeName(node: Node): String {
        return when (node) {
            is MethodCallExpr -> "${node.nameAsString}()"
            is ObjectCreationExpr -> "new ${node.typeAsString}()"
            is NameExpr -> node.nameAsString
            is FieldAccessExpr -> node.nameAsString
            else -> "Unknown node type not resolvable"
        }
    }
}