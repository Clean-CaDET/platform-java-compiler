package first_pass.node_parser

import cadet_model.CadetClass
import cadet_model.CadetMember
import cadet_model.CadetMemberType
import cadet_model.CadetParameter
import com.github.javaparser.ast.Node
import com.github.javaparser.ast.body.ConstructorDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import java.lang.IllegalArgumentException

object MemberDeclarationParser : AbstractNodeParser() {

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
        }
    }

    private fun getParameters(node: Node): List<CadetParameter> {
        val params = mutableListOf<CadetParameter>()
        getChildrenByType<Parameter>(node)
            .forEach {
                params.add(CadetParameter(it.nameAsString, it.typeAsString))
            }
        return params
    }
}