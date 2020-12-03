package resolver.nodes.common

import com.github.javaparser.ast.expr.NullLiteralExpr
import resolver.SymbolResolver
import resolver.nodes.abs.BaseSolverNode

class NullSolverNode(node: NullLiteralExpr) : BaseSolverNode(node) {

    override fun resolve() {
        this.returnType = SymbolResolver.WildcardType
    }
}
