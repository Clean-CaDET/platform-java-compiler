package second_pass.resolver.node_parser

import cadet_model.CadetLocalVariable
import com.github.javaparser.ast.body.VariableDeclarator

object LocalVariableParser {

    fun instantiateLocalVariable(node: VariableDeclarator): CadetLocalVariable {
        return CadetLocalVariable(
            node.nameAsString,
            node.typeAsString
        )
    }
}