package resolver.nodes

import com.github.javaparser.ast.expr.SuperExpr
import resolver.SymbolMap
import resolver.SymbolResolver
import resolver.nodes.abs.ReferenceSolverNode

class SuperSolverNode(node: SuperExpr, symbolMap: SymbolMap) : ReferenceSolverNode(node, symbolMap) {

    override fun resolve() {
        symbolMap.getContextSuperType()
            ?.let {
                returnType = it
                return
            }
        returnType = SymbolResolver.WildcardType
    }
}
