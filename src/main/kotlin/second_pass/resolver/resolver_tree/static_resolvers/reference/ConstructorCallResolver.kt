package second_pass.resolver.resolver_tree.static_resolvers.reference

import cadet_model.CadetMember
import com.github.javaparser.ast.expr.ObjectCreationExpr
import second_pass.resolver.InjectedContext
import second_pass.resolver.resolver_tree.model.ReferenceNode
import second_pass.signature.MemberSignature
import second_pass.signature.SignableMember

object ConstructorCallResolver {

    fun resolve(node: ReferenceNode, injectedContext: InjectedContext): CadetMember? {
        val sigWrapper = object: SignableMember {

            override fun name(): String
                = (node.astNode as ObjectCreationExpr).typeAsString

            override fun getParameterTypes(): List<String>
                = node.children.map { childArg -> childArg.returnType }

            override fun getNumberOfParameters(): Int
                = node.children.size
        }

        return injectedContext.getConstructor(MemberSignature(sigWrapper))
    }


}
