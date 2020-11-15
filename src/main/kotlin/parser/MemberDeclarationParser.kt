package parser

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.body.ConstructorDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import model.CadetClass
import model.CadetMember
import model.CadetMemberType
import model.CadetParameter

internal object MemberDeclarationParser : AbstractNodeParser() {

    /**
     *  Instantiates a [CadetMember] object with type [CadetMemberType.Method], and all the data
     *  available from the given [node]
     */
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

    /**
     *  Instantiates a [CadetMember] object with type [CadetMemberType.Constructor], and all the data
     *  available from the given [node]
     */
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