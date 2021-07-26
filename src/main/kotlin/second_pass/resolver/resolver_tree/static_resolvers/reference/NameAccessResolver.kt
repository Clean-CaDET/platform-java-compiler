package second_pass.resolver.resolver_tree.static_resolvers.reference

import cadet_model.abs.CadetVariable
import com.github.javaparser.ast.expr.NameExpr
import second_pass.resolver.InjectedContext
import second_pass.resolver.resolver_tree.model.ReferenceNode

object NameAccessResolver {

    fun resolve(node: ReferenceNode, injectedContext: InjectedContext): CadetVariable? {
        return injectedContext.getVariableInScope((node.astNode as NameExpr).nameAsString)
    }
}
