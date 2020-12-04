package second_pass.resolver.solver_nodes.common

import com.github.javaparser.ast.expr.SuperExpr
import second_pass.resolver.SymbolSolvingBundle
import second_pass.resolver.solver_nodes.abs.BaseSolverNode
import java.lang.IllegalArgumentException

class SuperSolverNode(node: SuperExpr, private val symbolSolvingBundle: SymbolSolvingBundle)
    : BaseSolverNode(node)
{
    override fun resolve() {
        symbolSolvingBundle.getCurrentClassSuperType()
            .let {
                it ?: throw IllegalArgumentException("'Super' not resolvable. Cannot find parent.")
                returnType = it
            }
    }
}
