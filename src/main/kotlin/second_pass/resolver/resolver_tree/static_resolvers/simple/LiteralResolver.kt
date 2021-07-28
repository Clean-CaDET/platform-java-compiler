package second_pass.resolver.resolver_tree.static_resolvers.simple

import com.github.javaparser.ast.expr.*
import second_pass.resolver.ResolverProxy
import second_pass.resolver.resolver_tree.service.Resolver

object LiteralResolver {

    fun resolve(astNode: LiteralExpr): String {
        return when (astNode) {
            is IntegerLiteralExpr -> "int"
            is DoubleLiteralExpr -> "double"
            is CharLiteralExpr -> "char"
            is StringLiteralExpr -> "String"
            is BooleanLiteralExpr -> "boolean"
            is LongLiteralExpr -> "long"
            is NullLiteralExpr -> Resolver.WildcardType
            else -> throw IllegalArgumentException("Unrecognized node type in resolver: ${astNode.metaModel.typeName}.")
        }
    }
}