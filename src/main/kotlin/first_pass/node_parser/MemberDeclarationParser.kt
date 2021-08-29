package first_pass.node_parser

import cadet_model.*
import com.github.javaparser.ast.Modifier
import com.github.javaparser.ast.Node
import com.github.javaparser.ast.body.ConstructorDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import util.AST

object MemberDeclarationParser {

    fun instantiateMethod(node: MethodDeclaration, parent: CadetClass): CadetMember {
        return instantiateBaseMember(node, parent, CadetMemberType.Method).apply {
            this.name = node.nameAsString
            this.returnType = node.typeAsString
        }
    }

    fun instantiateConstructor(node: ConstructorDeclaration, parent: CadetClass): CadetMember {
        return instantiateBaseMember(node, parent, CadetMemberType.Constructor).apply {
            this.name = node.nameAsString
            this.returnType = node.nameAsString
        }
    }

    private fun instantiateBaseMember(node: Node, parent: CadetClass, type: CadetMemberType): CadetMember {
        return CadetMember().apply {
            this.parent = parent
            this.cadetMemberType = type
            getParameters(node).forEach {
                this.params.add(it)
            }
            getModifiers(node).forEach {
                this.modifiers.add(CadetModifier(it))
            }
        }
    }

    private fun getParameters(node: Node): List<CadetParameter> {
        return AST.getChildrenByType<Parameter>(node)
            .map { param -> CadetParameter(param.nameAsString, param.typeAsString) }
    }

    private fun getModifiers(node: Node): List<String> {
        return AST.getChildrenByType<Modifier>(node).map { it.toString() }
    }
}