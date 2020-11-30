package parser.node

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.expr.FieldAccessExpr
import com.github.javaparser.ast.expr.SimpleName

object FieldAccessExpressionParser : AbstractNodeParser() {

    fun getFieldName(node: FieldAccessExpr): String {
        return getChildByType<SimpleName>(node)!!.asString()
    }

    fun getCallerNode(node: Node): Node {
        node.childNodes[0]
            .also {
                if (it is SimpleName) throw IllegalCallerException("Accessing simple name instead of caller")
                return it
            }
    }
}