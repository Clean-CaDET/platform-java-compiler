package second_pass.resolver.solver_nodes.common

import com.github.javaparser.ast.expr.CastExpr
import second_pass.resolver.SymbolSolvingBundle
import second_pass.resolver.SymbolResolver
import second_pass.resolver.solver_nodes.abs.BaseSolverNode

class CastSolverNode(node: CastExpr, private val symbolSolvingBundle: SymbolSolvingBundle)
    : BaseSolverNode(node)
{
    override fun resolve() {
        this.returnType = (node as CastExpr).typeAsString

        node.childNodes.forEach { child ->
            SymbolResolver.createSolverNode(child, symbolSolvingBundle)?.resolve()
        }
    }
}