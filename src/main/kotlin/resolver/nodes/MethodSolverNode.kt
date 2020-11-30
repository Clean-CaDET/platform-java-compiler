package resolver.nodes

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.expr.MethodCallExpr
import parser.node.MethodCallExpressionParser
import resolver.SymbolMap
import resolver.SymbolResolver
import resolver.nodes.abs.CallSolverNode

class MethodSolverNode(
    node: MethodCallExpr,
    symbolMap: SymbolMap
) : CallSolverNode(node, symbolMap) {

    override var caller: Node?
        get() = MethodCallExpressionParser.getCaller(node as MethodCallExpr)
        set(value) {}

    override fun resolve() {
        super.resolve()

        if (resolvedReference != null)
            this.returnType = resolvedReference!!.returnType
        else
            resolveIfObjectMethod()
    }

    private fun resolveIfObjectMethod() {
        returnType = when (getName()) {
            "toString" -> "String"
            "clone" -> "Object"
            "hashCode" -> "int"
            "equals" -> "boolean"
            "getClass" -> callerResolverNode!!.returnType
            else -> SymbolResolver.WildcardType
        }
    }

    override fun getName(): String = (node as MethodCallExpr).nameAsString
}