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
        callResolveReference()?.let {
            resolvedReference = it
            returnType = it.returnType
        }
    }

    protected abstract fun callResolveReference(): CadetMember?
    protected abstract fun initChildCondition(child: Node): Boolean

    private fun initArgumentNodes() {
        node.childNodes.forEach { child ->
            if (initChildCondition(child)) {
                SymbolResolver.createSolverNode(child, symbolMap)
                    ?.let { this.children.add(it) }
            }
        }
    }

    override fun getParameterTypes(): List<String> {
        return mutableListOf<String>()
            .also { list ->
                children.forEach { list.add(it.returnType) }
            }
    }
    override fun getNumberOfParameters(): Int = children.size
}