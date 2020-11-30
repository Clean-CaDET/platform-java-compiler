package resolver.nodes.cadet

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.expr.MethodCallExpr
import model.CadetMember
import parser.node.MethodCallExpressionParser
import resolver.SymbolContextMap
import resolver.SymbolResolver
import resolver.nodes.abs.MemberCallSolverNode

class MethodSolverNode(
    node: MethodCallExpr,
    symbolMap: SymbolContextMap
) : MemberCallSolverNode(node, symbolMap) {

    override var caller: Node?
        get() = MethodCallExpressionParser.getCaller(node as MethodCallExpr)
        set(value) {}

    override fun resolve() {
        super.resolve()

        if (resolvedReference != null)
            this.returnType = (resolvedReference as CadetMember).returnType
    }

    override fun getName(): String = (node as MethodCallExpr).nameAsString
}