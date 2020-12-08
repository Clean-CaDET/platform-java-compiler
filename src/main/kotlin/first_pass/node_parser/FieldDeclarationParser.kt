package first_pass.node_parser

import cadet_model.CadetClass
import cadet_model.CadetField
import cadet_model.CadetLocalVariable
import cadet_model.CadetModifier
import com.github.javaparser.ast.Modifier
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.VariableDeclarator

object FieldDeclarationParser : AbstractNodeParser() {

    fun instantiateClassField(node: FieldDeclaration, parent: CadetClass): CadetField {
        return CadetField().apply {
            this.name = getFieldName(node)
            this.parent = parent
            getModifiers(node).forEach {
                this.modifiers.add(it)
            }
            this.type = node.elementType.asString()
        }
    }

    // TODO Move this out into a separate parser class?
    fun instantiateLocalVariable(node: VariableDeclarator): CadetLocalVariable {
        return CadetLocalVariable(
            node.nameAsString,
            node.typeAsString
        )
    }

    private fun getFieldName(node: FieldDeclaration): String {
        getChildByType<VariableDeclarator>(node)
            .also {
                return (it as VariableDeclarator).nameAsString
            }
    }

    private fun getModifiers(node: FieldDeclaration): List<CadetModifier> {
        val modifiers = mutableListOf<CadetModifier>()
        getChildrenByType<Modifier>(node)
            .forEach {
                modifiers.add(CadetModifier(it.keyword.asString()))
            }
        return modifiers
    }
}