package resolver.nodes.abs

import com.github.javaparser.ast.Node
import model.CadetMember
import resolver.SymbolContextMap
import resolver.SymbolResolver
import signature.MemberSignature
import signature.SignableMember

abstract class MemberCallSolverNode(node: Node, symbolMap: SymbolContextMap)
    : WithCallerSolverNode<CadetMember>(node, symbolMap),
    SignableMember
{
    private val children = mutableListOf<BaseSolverNode>()

    override fun doResolve() {
        initArgumentNodes()
        children.forEach { it.resolve() }
        resolvedReference =
            symbolMap.getMember(callerResolverNode?.returnType, MemberSignature(this))
    }

    private fun initArgumentNodes() {
        node.childNodes.forEach { child ->
            if (child !== caller)
                SymbolResolver.createSolverNode(child, symbolMap)
                    ?.let { this.children.add(it) }
        }
    }

    // Member signature overriden methods
    override fun getParameterTypes(): List<String> {
        return mutableListOf<String>()
        .apply {
            children.forEach { child -> this.add(child.returnType) }
        }
    }
    override fun getNumberOfParameters(): Int = children.size
}