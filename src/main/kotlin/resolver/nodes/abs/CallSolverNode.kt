package resolver.nodes.abs

import com.github.javaparser.ast.Node
import model.CadetMember
import resolver.SymbolMap
import resolver.SymbolResolver
import signature.MemberSignature
import signature.SignableMember

abstract class CallSolverNode(node: Node, symbolMap: SymbolMap)
    : ReferenceSolverNode(node, symbolMap),
    SignableMember
{
    private val children = mutableListOf<BaseSolverNode>()

    protected var caller: Node? = null
    protected var callerResolverNode: BaseSolverNode? = null

    private var resolvedReference: CadetMember? = null

    /**
     * @return Resolved [CadetMember] reference.
     * @throws IllegalAccessError If [resolve] hasn't been called, or if resolving failed to find
     * the appropriate CadetMember in the SymbolMap
     */
    fun getResolvedReference(): CadetMember {
        resolvedReference ?: throw IllegalAccessError()
        return resolvedReference!!
    }

    override fun resolve() {
        initArgumentNodes()
        children.forEach { child ->
            child.resolve()
        }
        resolvedReference = symbolMap.findCadetMemberInContext(callerResolverNode?.returnType, MemberSignature(this))
        resolvedReference?.let { this.returnType = resolvedReference!!.returnType }
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
        .apply {
            this!!.resolve()
            callerResolverNode = this
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