package resolver.nodes.common

import com.github.javaparser.ast.expr.CastExpr
import resolver.nodes.abs.BaseSolverNode

class CastSolverNode(node: CastExpr) : BaseSolverNode(node) {
    override fun resolve() {
        this.returnType = (node as CastExpr).typeAsString   // TODO Iterate children
    }
}