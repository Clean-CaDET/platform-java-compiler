package resolver.nodes.cadet

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.expr.MethodCallExpr
import model.CadetMember
import parser.node.MethodCallExpressionParser
import resolver.SymbolContextMap
import resolver.nodes.abs.MemberCallSolverNode

class MethodSolverNode(
    node: MethodCallExpr,
    symbolMap: SymbolContextMap
) : MemberCallSolverNode(node, symbolMap) {

    override var caller: Node? = MethodCallExpressionParser.getCaller(node)

    override fun doResolve() {
        super.doResolve()

        if (resolvedReference != null)
            this.returnType = (resolvedReference as CadetMember).returnType
    }

    override fun getName(): String = (node as MethodCallExpr).nameAsString
}