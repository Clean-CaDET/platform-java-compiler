package second_pass.resolver.resolver_tree.type_resolvers.simple

import com.github.javaparser.ast.expr.CastExpr

object CastResolver {

    fun resolve(astNode: CastExpr): String {
        return astNode.typeAsString
    }
}