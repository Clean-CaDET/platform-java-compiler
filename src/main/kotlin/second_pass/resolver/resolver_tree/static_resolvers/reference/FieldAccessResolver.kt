package second_pass.resolver.resolver_tree.static_resolvers.reference

import cadet_model.abs.CadetVariable
import com.github.javaparser.ast.expr.FieldAccessExpr
import second_pass.resolver.InjectedContext
import second_pass.resolver.node_parser.FieldAccessExpressionParser
import second_pass.resolver.resolver_tree.model.ReferenceNode

object FieldAccessResolver {

    fun resolve(node: ReferenceNode, injectedContext: InjectedContext): CadetVariable? {
        return injectedContext.getField(
            node.children[0].returnType,
            FieldAccessExpressionParser.getVariableName(node.astNode as FieldAccessExpr)
        )
    }
}
