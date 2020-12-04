package second_pass.resolver.solver_nodes.cadet

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.expr.MethodCallExpr
import cadet_model.CadetMember
import first_pass.node_parser.MethodCallExpressionParser
import second_pass.resolver.SymbolSolvingBundle
import second_pass.resolver.solver_nodes.abs.MemberCallSolverNode
import second_pass.signature.MemberSignature

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