package second_pass.resolver.resolver_tree.static_resolvers.reference

import cadet_model.CadetMember
import com.github.javaparser.ast.expr.MethodCallExpr
import second_pass.resolver.InjectedContext
import second_pass.resolver.node_parser.MethodCallExpressionParser
import second_pass.resolver.resolver_tree.model.ReferenceNode
import second_pass.signature.MemberSignature
import second_pass.signature.SignableMember

object MethodCallResolver {

    fun resolve(node: ReferenceNode, injectedContext: InjectedContext): CadetMember? {
        val hasCaller: Boolean = MethodCallExpressionParser.hasCaller(node.astNode as MethodCallExpr)
        val numOfArgumentChildren = calculateNumberOfArgumentChildren(node, hasCaller)

        val sigWrapper = object: SignableMember {

            override fun name(): String =
                node.astNode.nameAsString

            override fun getParameterTypes(): List<String> =
                node.children.takeLast(numOfArgumentChildren)
                    .map { childArg -> childArg.returnType }

            override fun getNumberOfParameters(): Int =
                numOfArgumentChildren
        }

        val caller: String? = if (hasCaller) node.children[0].returnType else null

        return injectedContext.getMethod(MemberSignature(sigWrapper), caller)
    }

    private fun calculateNumberOfArgumentChildren(node: ReferenceNode, hasCaller: Boolean): Int {
        var numOfArgumentChildren = node.children.size
        if (hasCaller) numOfArgumentChildren--
        return numOfArgumentChildren
    }
}
