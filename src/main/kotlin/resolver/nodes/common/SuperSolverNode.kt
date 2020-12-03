package resolver.nodes.common

import com.github.javaparser.ast.expr.SuperExpr
import resolver.SymbolContextMap
import resolver.SymbolResolver
import resolver.nodes.abs.ReferenceSolverNode
import java.lang.IllegalArgumentException

class SuperSolverNode(node: SuperExpr, symbolMap: SymbolContextMap) : ReferenceSolverNode(node, symbolMap) {

    override fun resolve() {
        symbolMap.getContextClassSuperType()
            .let {
                it ?: throw IllegalArgumentException("'Super' not resolvable. Cannot find parent.")
                returnType = it
            }
    }
}
