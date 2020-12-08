package second_pass.resolver.solver_nodes.abs

import com.github.javaparser.ast.Node
import second_pass.resolver.SymbolResolver

abstract class WithCallerSolverNode<T : Any>(node: Node, resolver: SymbolResolver)
    : CadetSolverNode<T>(node, resolver)
{
    protected abstract var caller: Node?
    protected var callerResolverNode: BaseSolverNode? = null

    final override fun resolve() {
        resolveCaller()
        super.resolve()
    }

    private fun resolveCaller() {
        caller ?: return
        resolver.createSolverNode(caller!!)
            ?.let {
                callerResolverNode = it
                it.resolve()
            }
    }
}