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
            // In certain Solver scenarios such as when working with a LiteralNode, BaseSolver's return type will be
            // known without actually resolving the reference
            // However, returnType is lateinit so if it is not initialized, this will throw an exception
            try {
                returnType
                return
            }
            catch (e: UninitializedPropertyAccessException) {
                returnType = SymbolResolver.WildcardType
            }
        }
        else    // TODO Resolver.notifyUsage()? To zvuci lose
            resolver.getVisitorContext().notifyUsage(resolvedReference!!)
    }

    override fun resolve() {
        doResolve()
        notifyContextOfUsage()

        if (resolvedReference == null) print("FAILED:  ")
        else print("SUCCESS:  ")
        showNodeDetails(node)
    }

    // TODO Diagnostics for failed resolving, delete for prod
    private fun showNodeDetails(node: Node) {
        println("${node.metaModel.typeName} : '${nodeName(node)}' from ${
                resolver.getVisitorContext().getCurrentClassName()
            }"
        )
    }
    private fun nodeName(node: Node): String {
        return when (node) {
            is MethodCallExpr -> "${node.nameAsString}()"
            is ObjectCreationExpr -> "new ${node.typeAsString}()"
            is NameExpr -> node.nameAsString
            is FieldAccessExpr -> node.nameAsString
            else -> "${node.metaModel.typeName} node type not resolvable"
        }
    }
}