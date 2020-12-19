package second_pass.resolver.node_parser

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.expr.FieldAccessExpr
import com.github.javaparser.ast.expr.SimpleName
import first_pass.node_parser.AbstractNodeParser

object FieldAccessExpressionParser : AbstractNodeParser() {

    fun getCallerNode(node: FieldAccessExpr): Node {
        node.childNodes.forEach { child ->
            if (child !is SimpleName) return child
        }
        throw IllegalArgumentException("Field access has no caller.")
    }

    fun getVariableName(node: FieldAccessExpr): String {
        node.childNodes.filterIsInstance<SimpleName>()
            .apply {
                if (this.isEmpty()) throw IllegalArgumentException("Field access variable has no name.")
                return this[0].asString()
            }
    }
}