package resolver.nodes.cadet

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.expr.MethodCallExpr
import model.CadetMember
import parser.node.MethodCallExpressionParser
import resolver.SymbolSolvingBundle
import resolver.nodes.abs.MemberCallSolverNode
import signature.MemberSignature

class MethodSolverNode(
    node: MethodCallExpr,
    symbolMap: SymbolSolvingBundle
) : MemberCallSolverNode(node, symbolMap) {

    override var caller: Node? = MethodCallExpressionParser.getCaller(node)

    override fun callResolveReference(): CadetMember? {
        return symbolSolvingBundle.getMethod(callerResolverNode?.returnType, MemberSignature(this))
    }

    override fun initChildCondition(child: Node): Boolean = child !== caller

    override fun getName(): String = (node as MethodCallExpr).nameAsString
}