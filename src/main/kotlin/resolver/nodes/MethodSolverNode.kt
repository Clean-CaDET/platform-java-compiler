package resolver.nodes

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.expr.MethodCallExpr
import resolver.SymbolMap
import resolver.nodes.abs.CallSolverNode

class MethodSolverNode(
    node: MethodCallExpr,
    caller: Pair<Node, String?>?,
    symbolMap: SymbolMap
) : CallSolverNode(node, symbolMap) {

    init {
        this.caller = caller
    }
    override fun getName(): String = (node as MethodCallExpr).nameAsString
}