package resolver.nodes

import com.github.javaparser.ast.expr.NameExpr
import resolver.SymbolMap
import resolver.nodes.abs.BaseSolverNode
import resolver.nodes.abs.ReferenceSolverNode

class NameExpressionSolverNode(
    node: NameExpr,
    symbolMap: SymbolMap
): ReferenceSolverNode(node, symbolMap) {

    override fun resolve() {
        this.returnType = symbolMap.getContextScopedType((node as NameExpr).nameAsString);
    }
}