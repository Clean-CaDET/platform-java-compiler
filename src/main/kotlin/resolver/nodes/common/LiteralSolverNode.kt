package resolver.nodes.common

import com.github.javaparser.ast.expr.*
import resolver.SymbolResolver
import resolver.nodes.abs.BaseSolverNode
import java.lang.IllegalArgumentException

class LiteralSolverNode(node: LiteralExpr) : BaseSolverNode(node) {

    override fun resolve() {
        this.returnType = when(node) {
            is IntegerLiteralExpr -> "int"
            is DoubleLiteralExpr -> "double"
            is CharLiteralExpr -> "char"
            is StringLiteralExpr -> "String"
            is BooleanLiteralExpr -> "boolean"
            is LongLiteralExpr -> "long"
            is NullLiteralExpr -> SymbolResolver.WildcardType
            else -> throw IllegalArgumentException("Unrecognized node type in resolver: ${node.metaModel.typeName}.")
        }
    }
}
