package parser

import com.github.javaparser.ast.Modifier
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.VariableDeclarator
import model.CadetClass
import model.CadetField
import model.CadetLocalVariable
import model.CadetModifier

internal object FieldDeclarationParser : AbstractNodeParser() {

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

    fun instantiateLocalField(node: VariableDeclarator): CadetLocalVariable {
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