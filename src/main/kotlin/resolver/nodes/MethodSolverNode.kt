package resolver.nodes

import com.github.javaparser.ast.expr.MethodCallExpr
import parser.node.MethodCallExpressionParser
import resolver.SymbolMap
import resolver.nodes.abs.CallSolverNode

class MethodSolverNode(
    node: MethodCallExpr,
    symbolMap: SymbolMap
) : CallSolverNode(node, symbolMap) {

    init {
        this.caller = MethodCallExpressionParser.getCaller(node)
    }
    override fun getName(): String = (node as MethodCallExpr).nameAsString
}