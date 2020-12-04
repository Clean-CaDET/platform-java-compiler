package first_pass.node_parser

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.body.ConstructorDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import cadet_model.CadetClass
import cadet_model.CadetMember
import cadet_model.CadetMemberType
import cadet_model.CadetParameter

object MemberDeclarationParser : AbstractNodeParser() {

    fun instantiateMethod(node: MethodDeclaration, parent: CadetClass): CadetMember {
        return CadetMember().apply {
            this.name = node.nameAsString
            this.parent = parent
            this.cadetMemberType = CadetMemberType.Method
            getParameters(node).forEach {
                this.params.add(it)
            }
            this.returnType = node.typeAsString
        }
    }

    fun instantiateConstructor(node: ConstructorDeclaration, parent: CadetClass): CadetMember {
        return CadetMember().apply {
            this.name = node.nameAsString
            this.parent = parent
            this.cadetMemberType = CadetMemberType.Constructor
            getParameters(node).forEach {
                this.params.add(it)
            }
            this.returnType = node.nameAsString
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