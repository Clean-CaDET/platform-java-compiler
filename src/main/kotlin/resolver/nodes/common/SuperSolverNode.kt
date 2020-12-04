package resolver.nodes.common

import com.github.javaparser.ast.expr.SuperExpr
import resolver.SymbolSolvingBundle
import resolver.nodes.abs.BaseSolverNode
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
