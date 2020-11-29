package resolver.nodes.abs

import com.github.javaparser.ast.Node
import model.CadetMember
import resolver.SymbolMap
import resolver.SymbolResolver
import signature.MemberSignature
import signature.SignableMember

abstract class CallSolverNode(node: Node, private val symbolMap: SymbolMap)
    : ResolvableNode(node),
    SignableMember
{
    private val children = mutableListOf<BaseSolverNode>()
    protected var caller: Pair<Node, String?>? = null

    private var resolvedReference: CadetMember? = null
    fun getResolvedReference() = resolvedReference

    override fun resolve() {
        initArgumentNodes()
        children.filterIsInstance<ResolvableNode>()
            .apply {
                this.forEach { child -> child.resolve() }
            }
        resolvedReference = symbolMap.getCadetMember(caller?.second, MemberSignature(this))
        resolvedReference?.let { this.returnType = resolvedReference!!.returnType }
    }

    private fun initArgumentNodes() {
        node.childNodes.forEach { child ->
            if (child === caller?.first) return@forEach
            SymbolResolver.createSolverNode(child, symbolMap)
                ?.let {
                    this.children.add(it)
                }
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