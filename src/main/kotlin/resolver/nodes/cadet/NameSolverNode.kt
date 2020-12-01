package resolver.nodes.cadet

import com.github.javaparser.ast.expr.NameExpr
import model.abs.CadetVariable
import resolver.SymbolContextMap
import resolver.nodes.abs.CadetSolverNode

class NameSolverNode(
    node: NameExpr,
    symbolMap: SymbolContextMap
): CadetSolverNode<CadetVariable>(node, symbolMap) {

    override fun doResolve() {
        symbolMap.getVariableInContext((node as NameExpr).nameAsString)
            ?.let {
                this.resolvedReference = it
                this.returnType = it.type
                return
            }
        returnType = node.nameAsString  // Class names in static access are treated as NameExpr
        // TODO Does this have children which are being called/accessed?
    }
}