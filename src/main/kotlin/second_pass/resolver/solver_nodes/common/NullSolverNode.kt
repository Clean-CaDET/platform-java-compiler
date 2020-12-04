package second_pass.resolver.solver_nodes.common

import com.github.javaparser.ast.expr.NullLiteralExpr
import second_pass.resolver.SymbolResolver
import second_pass.resolver.solver_nodes.abs.BaseSolverNode

class NullSolverNode(node: NullLiteralExpr) : BaseSolverNode(node) {

    override fun resolve() {
        this.returnType = SymbolResolver.WildcardType
    }
}
