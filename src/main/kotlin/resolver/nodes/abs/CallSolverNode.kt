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

    protected abstract var caller: Node?
    protected var callerResolverNode: BaseSolverNode? = null

    protected var resolvedReference: CadetMember? = null
    /**
     * @return Resolved [CadetMember] reference.
     * @throws IllegalAccessError If [resolve] hasn't been called, or if resolving failed to find
     * the appropriate CadetMember in the SymbolMap
     */
    fun getResult(): CadetMember {
        resolvedReference ?: throw IllegalAccessError()
        return resolvedReference!!
    }

    override fun resolve() {
        initArgumentNodes()
        children.forEach { child ->
            child.resolve()
        }
        resolvedReference =
            symbolMap.findCadetMemberInContext(callerResolverNode?.returnType, MemberSignature(this))
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