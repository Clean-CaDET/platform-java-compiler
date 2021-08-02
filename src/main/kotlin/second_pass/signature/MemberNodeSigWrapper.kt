package second_pass.signature

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.body.ConstructorDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import util.AST

class MemberNodeSigWrapper : SignableMember {
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

    override fun name(): String = name
    override fun getParameterTypes(): List<String> = parameterTypes
    override fun getNumberOfParameters(): Int = parameterTypes.size

    private fun getParameterTypes(node: Node) {
        AST.getChildrenByType<Parameter>(node).forEach { paramNode ->
            parameterTypes.add(paramNode.typeAsString)
        }
    }
}