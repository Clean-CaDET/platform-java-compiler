package second_pass.resolver.solver_nodes.common

import com.github.javaparser.ast.expr.EnclosedExpr
import second_pass.resolver.SymbolResolver
import second_pass.resolver.solver_nodes.abs.BaseSolverNode

class EnclosedExprSolverNode(node: EnclosedExpr, private val resolver: SymbolResolver)
    : BaseSolverNode(node)
{
    override fun resolve() {
        val childSolverNode = resolver.createSolverNode(node.childNodes[0])
        returnType = if (childSolverNode != null)
        {
            childSolverNode.resolve()
            childSolverNode.returnType
        }
        else SymbolResolver.WildcardType
    }
}
