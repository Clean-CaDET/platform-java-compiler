package resolver.nodes.abs

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.expr.MethodCallExpr
import model.CadetMember
import resolver.SymbolContextMap
import resolver.SymbolResolver
import signature.MemberSignature
import signature.SignableMember

abstract class MemberCallSolverNode(node: Node, symbolMap: SymbolContextMap)
    : CadetSolverNode<CadetMember>(node, symbolMap),
    SignableMember
{
    private val children = mutableListOf<BaseSolverNode>()

    /**
     * @return Resolved [CadetMember] reference.
     * @throws IllegalAccessError If [resolve] hasn't been called, or if resolving failed to find
     * the appropriate CadetMember in the SymbolMap
     */
    fun getResult(): CadetMember {
        resolvedReference ?: throw IllegalAccessError()
        return resolvedReference as CadetMember
    }

    override fun resolve() {
        initArgumentNodes()
        children.forEach { it.resolve() }
        resolvedReference =
            symbolMap.getCadetMemberInContext(callerResolverNode?.returnType, MemberSignature(this))
    }

    private fun initArgumentNodes() {
        node.childNodes.forEach { child ->
            if (child === caller)
                resolveCaller(child)
            else
                SymbolResolver.createSolverNode(child, symbolMap)
                ?.let {
                    this.children.add(it)
                }
        }
    }

    private fun resolveCaller(caller: Node) {
        SymbolResolver.createSolverNode(caller, symbolMap)
        .also {
            it!!.resolve()
            callerResolverNode = it
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