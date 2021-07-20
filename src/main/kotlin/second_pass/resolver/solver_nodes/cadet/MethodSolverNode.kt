package second_pass.resolver.solver_nodes.cadet

import cadet_model.CadetMember
import com.github.javaparser.ast.Node
import com.github.javaparser.ast.expr.MethodCallExpr
import second_pass.resolver.node_parser.MethodCallExpressionParser
import second_pass.resolver.SymbolResolver
import second_pass.resolver.solver_nodes.abs.MemberCallSolverNode
import second_pass.signature.MemberSignature

class MethodSolverNode(
    node: MethodCallExpr,
    resolver: SymbolResolver
) : MemberCallSolverNode(node, resolver) {

    override var caller: Node? = MethodCallExpressionParser.getCaller(node)

    override fun resolveViaSignature(): CadetMember? {
        return resolver.getMethod(
            callerResolverNode?.returnType,
            MemberSignature(this).withHierarchyGraph(resolver.getHierarchyGraph())
        )
    }

    // TODO This is messy, try to find a way around it
    override fun initChildCondition(child: Node): Boolean = child !== caller

    override fun name(): String = (node as MethodCallExpr).nameAsString
}