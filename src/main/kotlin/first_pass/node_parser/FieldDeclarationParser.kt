package first_pass.node_parser

import cadet_model.CadetClass
import cadet_model.CadetField
import cadet_model.CadetModifier
import com.github.javaparser.ast.Modifier
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.VariableDeclarator
import util.AST

object FieldDeclarationParser {

    fun instantiateField(node: FieldDeclaration, parent: CadetClass): CadetField {
        return CadetField().apply {
            this.name = getFieldName(node)
            this.parent = parent
            getModifiers(node).forEach {
                this.modifiers.add(it)
            }
            this.type = node.elementType.asString()
        }
    }

    private fun getFieldName(node: FieldDeclaration): String {
        val varDeclaratorNode = AST.getChildByType<VariableDeclarator>(node)
        return varDeclaratorNode!!.nameAsString
    }

    private fun getModifiers(node: FieldDeclaration): List<CadetModifier> {
        return AST.getChildrenByType<Modifier>(node)
            .map { child -> CadetModifier(child.keyword.asString()) }
    }
}