package resolver.nodes.common

import com.github.javaparser.ast.expr.CastExpr
import resolver.SymbolContextMap
import resolver.SymbolResolver
import resolver.nodes.abs.ReferenceSolverNode

class CastSolverNode(node: CastExpr, symbolMap: SymbolContextMap) : ReferenceSolverNode(node, symbolMap) {
    override fun resolve() {
        this.returnType = (node as CastExpr).typeAsString

        node.childNodes.forEach { child ->
            SymbolResolver.createSolverNode(child, symbolMap)?.resolve()
        }
    }
}