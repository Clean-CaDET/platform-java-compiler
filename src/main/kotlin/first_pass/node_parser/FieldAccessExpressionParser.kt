package first_pass.node_parser

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.expr.FieldAccessExpr
import com.github.javaparser.ast.expr.SimpleName

object FieldAccessExpressionParser : AbstractNodeParser() {

    fun getFieldName(node: FieldAccessExpr): String {
        return getChildByType<SimpleName>(node)!!.asString()
    }

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