package second_pass.resolver.resolver_tree.type_resolvers.reference

import cadet_model.abs.CadetVariable
import com.github.javaparser.ast.expr.FieldAccessExpr
import second_pass.resolver.ScopeContext
import second_pass.resolver.node_parser.FieldAccessExpressionParser
import second_pass.resolver.resolver_tree.ResolverTree

object FieldAccessResolver {

    fun resolve(node: ResolverTree.ReferenceNode, scopeContext: ScopeContext): CadetVariable? {
        return scopeContext.getField(
            node.children[0].returnType,
            FieldAccessExpressionParser.getVariableName(node.astNode as FieldAccessExpr)
        )
    }
}
