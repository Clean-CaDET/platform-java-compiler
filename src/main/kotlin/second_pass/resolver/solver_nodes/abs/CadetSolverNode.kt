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
                // In certain scenarios, return type will be known without reference resolving (e.g. LiteralSolverNode)
                // In these cases, we check whether the returnType is initialized. If not, we give it a Wildcard type
                returnType
            }
            catch (ignore: UninitializedPropertyAccessException) {
                returnType = SymbolResolver.WildcardType
            }
        }
        // TODO Remove comment and replace logic
//        else
//            resolver.getVisitorContext().notifyUsage(resolvedReference!!)
//                fun <T : Any> notifyUsage(resolvedReference: T) {
//                    when (resolvedReference) {
//                        is CadetMember -> addMemberInvocation(resolvedReference)
//                        is CadetField -> addFieldAccess(resolvedReference)
//                        is CadetParameter -> {}
//                        is CadetLocalVariable -> {}
//                        else -> throw IllegalArgumentException("Unsupported reference usage: $resolvedReference")
//                    }
//                }
    }

    override fun resolve() {
        doResolve()
        notifyContextOfUsage()
        diagnostics()
    }

    // TODO Diagnostics for failed resolving, delete for prod
    private fun diagnostics() {
        if (resolvedReference == null) print("FAILED:  ")
        else print("SUCCESS:  ")
        showNodeDetails(node)
    }


    private fun showNodeDetails(node: Node) {
        println("${node.metaModel.typeName} : '${nodeName(node)}' from ${
                resolver.getWizard().getCurrentClassName()
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