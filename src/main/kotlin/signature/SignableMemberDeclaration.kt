package signature

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.body.ConstructorDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import parser.AbstractNodeParser

internal class SignableMemberDeclaration
    : AbstractNodeParser,
      SignableMember
{
    private val name: String
    private val parameterTypes = mutableListOf<String>()

    constructor(node: MethodDeclaration) : super() {
        name = node.nameAsString
        getParameterTypes(node)
    }

    constructor(node: ConstructorDeclaration) : super() {
        name = node.nameAsString
        getParameterTypes(node)
    }

    override fun getName(): String = name
    override fun getParameterTypes(): List<String> = parameterTypes
    override fun getNumberOfParameters(): Int = parameterTypes.size

    private fun getParameterTypes(node: Node) {
        getChildrenByType<Parameter>(node).forEach { parameterNode ->
            parameterTypes.add(parameterNode.typeAsString)
        }
    }

}