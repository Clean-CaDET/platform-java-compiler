package second_pass.resolver.node_parser

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.expr.FieldAccessExpr
import com.github.javaparser.ast.expr.SimpleName
import util.AstNodeUtil

object FieldAccessExpressionParser {

    fun getCallerNode(node: FieldAccessExpr): Node {
        node.childNodes.forEach { child ->
            if (child !is SimpleName) return child
        }
        error("Field access has no caller.")
    }

    // TODO This might be replaceable with a simple "node.nameAsString" call?
    fun getVariableName(node: FieldAccessExpr): String {
        node.childNodes.filterIsInstance<SimpleName>()
            .apply {
                if (this.isEmpty()) error("Field access variable has no name.")
                return this[0].asString()
            }
    }
}