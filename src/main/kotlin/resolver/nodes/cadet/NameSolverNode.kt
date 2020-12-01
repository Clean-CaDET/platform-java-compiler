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
        .also {
            if (it != null) {
                this.resolvedReference = it
                this.returnType = it.type
            }
            else
                returnType = node.nameAsString
        }
    }
}