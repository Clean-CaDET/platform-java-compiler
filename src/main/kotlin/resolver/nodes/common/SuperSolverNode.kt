package resolver.nodes.common

import com.github.javaparser.ast.expr.SuperExpr
import resolver.SymbolContextMap
import resolver.SymbolResolver
import resolver.nodes.abs.ReferenceSolverNode

class SuperSolverNode(node: SuperExpr, symbolMap: SymbolContextMap) : ReferenceSolverNode(node, symbolMap) {

    override fun resolve() {
        symbolMap.getContextClassSuperType()
            ?.let {
                returnType = it
                return
            }
        returnType = SymbolResolver.WildcardType
    }
}
