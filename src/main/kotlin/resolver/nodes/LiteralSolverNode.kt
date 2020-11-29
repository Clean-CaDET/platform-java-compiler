package resolver.nodes

import com.github.javaparser.ast.expr.*
import resolver.nodes.abs.BaseSolverNode
import java.lang.IllegalArgumentException

class LiteralSolverNode(node: LiteralExpr) : BaseSolverNode() {

    init {
        this.returnType = when(node) {
            is IntegerLiteralExpr -> "int"
            is DoubleLiteralExpr -> "double"
            is CharLiteralExpr -> "char"
            is BooleanLiteralExpr -> "boolean"
            is LongLiteralExpr -> "long"
            is NullLiteralExpr -> throw IllegalArgumentException("Null as argument not supported.")
            else -> throw IllegalArgumentException("Unrecognized node type in resolver: ${node.metaModel.typeName}.")
        }
    }
}
