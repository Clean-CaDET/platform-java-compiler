package parser.node

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.expr.FieldAccessExpr
import com.github.javaparser.ast.expr.SimpleName
import java.lang.IllegalArgumentException

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
        node.childNodes.forEach { child ->
            if (child is SimpleName) return child.asString()
        }
        throw IllegalArgumentException("Field access variable has no name.")
    }
}