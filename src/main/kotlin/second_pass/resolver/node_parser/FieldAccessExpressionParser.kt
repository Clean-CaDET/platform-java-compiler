package second_pass.resolver.node_parser

import com.github.javaparser.ast.expr.FieldAccessExpr

object FieldAccessExpressionParser {

    fun getVariableName(node: FieldAccessExpr): String {
        return node.nameAsString
    }
}