package second_pass.resolver.resolver_tree.type_resolvers.reference

import cadet_model.abs.CadetVariable
import com.github.javaparser.ast.expr.NameExpr
import second_pass.resolver.ScopeContext
import second_pass.resolver.resolver_tree.ResolverTree

object NameAccessResolver {
    fun resolve(node: ResolverTree.ReferenceNode, scopeContext: ScopeContext): CadetVariable? {
        return scopeContext.getVariableInScope((node.astNode as NameExpr).nameAsString)
    }
}
