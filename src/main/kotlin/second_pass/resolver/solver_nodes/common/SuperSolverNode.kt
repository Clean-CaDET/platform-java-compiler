package second_pass.resolver.solver_nodes.common

import com.github.javaparser.ast.expr.SuperExpr
import second_pass.resolver.SymbolResolver
import second_pass.resolver.solver_nodes.abs.BaseSolverNode

class SuperSolverNode(node: SuperExpr, private val resolver: SymbolResolver) : BaseSolverNode(node) {
    override fun resolve() {
        resolver.getCurrentClassSuperType()
            .let {
                returnType = it ?: SymbolResolver.WildcardType
            }
    }
}
