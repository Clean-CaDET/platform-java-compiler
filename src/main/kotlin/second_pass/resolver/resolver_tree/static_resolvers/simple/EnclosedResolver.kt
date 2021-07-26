package second_pass.resolver.resolver_tree.static_resolvers.simple

import com.github.javaparser.ast.expr.EnclosedExpr

object EnclosedResolver {

    fun resolve(astNode: EnclosedExpr): String {
        return "Needs resolved children in order to do this" // TODO
    }
}
