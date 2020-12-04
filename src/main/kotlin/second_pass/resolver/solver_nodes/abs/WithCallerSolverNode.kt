package second_pass.resolver.solver_nodes.abs

import com.github.javaparser.ast.Node
import second_pass.resolver.SymbolSolvingBundle
import second_pass.resolver.SymbolResolver

abstract class WithCallerSolverNode<T>(node: Node, symbolSolvingBundle: SymbolSolvingBundle)
    : CadetSolverNode<T>(node, symbolSolvingBundle)
{
    protected abstract var caller: Node?
    protected var callerResolverNode: BaseSolverNode? = null

    final override fun resolve() {
        resolveCaller()
        super.resolve()
    }

    private fun resolveCaller() {
        caller ?: return
        SymbolResolver.createSolverNode(caller!!, symbolSolvingBundle)
            ?.let {
                callerResolverNode = it
                it.resolve()
            }
    }
}