package resolver.nodes.abs

import com.github.javaparser.ast.Node
import resolver.SymbolMap
import resolver.SymbolResolver
import signature.MemberSignature
import signature.SignableMember

abstract class CallSolverNode(node: Node, private val symbolMap: SymbolMap)
    : ReferenceSolverNode(node),
    SignableMember
{
    private val children = mutableListOf<BaseSolverNode>()
    protected var caller: Pair<Node, String?>? = null

    override fun resolve() {
        initArgumentNodes()
        children.filterIsInstance<ReferenceSolverNode>()
            .apply {
                this.forEach { child -> child.resolve() }
            }
        symbolMap.getCadetMemberReturnType(caller?.second, MemberSignature(this))
            ?.let {
                this.returnType = it
            }
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

    override fun getParameterTypes(): List<String> {
        return mutableListOf<String>()
        .apply {
            children.forEach { child -> this.add(child.returnType) }
        }
    }
    override fun getNumberOfParameters(): Int = children.size
}