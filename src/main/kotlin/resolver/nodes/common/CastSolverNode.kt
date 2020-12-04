package resolver.nodes.common

import com.github.javaparser.ast.expr.CastExpr
import resolver.SymbolSolvingBundle
import resolver.SymbolResolver
import resolver.nodes.abs.BaseSolverNode

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