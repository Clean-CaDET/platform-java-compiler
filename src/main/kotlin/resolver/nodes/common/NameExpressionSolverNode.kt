package resolver.nodes.common

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.expr.NameExpr
import model.interfaces.CadetVariable
import resolver.SymbolContextMap
import resolver.nodes.abs.CadetSolverNode

class NameExpressionSolverNode(
    node: NameExpr,
    symbolMap: SymbolContextMap
): CadetSolverNode<CadetVariable>(node, symbolMap) {

    override var caller: Node?
        get() = null
        set(value) {}

    override fun resolve() {
        symbolMap.getCadetVariableInContext((node as NameExpr).nameAsString)
            ?.let {
                this.resolvedReference = it
                this.returnType = it.type()
                return
            }
        returnType = node.nameAsString  // Class names in static access are treated as NameExpr
    }
}