package second_pass.resolver.solver_nodes.abs

import cadet_model.CadetMember
import com.github.javaparser.ast.Node
import second_pass.resolver.SymbolResolver
import second_pass.signature.SignableMember

abstract class MemberCallSolverNode(node: Node, resolver: SymbolResolver)
    : WithCallerSolverNode<CadetMember>(node, resolver),
    SignableMember
{
    private val arguments = mutableListOf<BaseSolverNode>()

    override fun doResolve() {
        initArgumentNodes()
        arguments.forEach { it.resolve() }
        resolveViaSignature()?.let {
            resolvedReference = it
            returnType = it.returnType
        }
    }

    protected abstract fun resolveViaSignature(): CadetMember?
    protected abstract fun initChildCondition(child: Node): Boolean

    private fun initArgumentNodes() {
        node.childNodes.forEach { child ->
            // TODO Add predefined child != caller condition?
            if (initChildCondition(child)) {
                resolver.createSolverNode(child)
                    ?.let { this.arguments.add(it) }
            }
        }
    }

    override fun getParameterTypes(): List<String> {
        return mutableListOf<String>()
            .also { list ->
                arguments.forEach { list.add(it.returnType) }
            }
    }

    override fun getNumberOfParameters(): Int = arguments.size
}