package second_pass.resolver.solver_nodes.abs

import cadet_model.CadetMember
import com.github.javaparser.ast.Node
import second_pass.resolver.SymbolResolver
import second_pass.signature.SignableMember

abstract class MemberCallSolverNode(node: Node, resolver: SymbolResolver) :
    WithCallerSolverNode<CadetMember>(node, resolver),
    SignableMember {
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
                resolver.createSolverNode(child)
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