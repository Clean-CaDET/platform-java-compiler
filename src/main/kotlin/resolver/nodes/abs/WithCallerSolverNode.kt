package resolver.nodes.abs

import com.github.javaparser.ast.Node
import resolver.SymbolContextMap
import resolver.SymbolResolver

abstract class WithCallerSolverNode<T>(node: Node, symbolContextMap: SymbolContextMap)
    : CadetSolverNode<T>(node, symbolContextMap)
{
    protected abstract var caller: Node?
    protected var callerResolverNode: BaseSolverNode? = null

    final override fun resolve() {
        resolveCaller()
        super.resolve()
    }

    private fun resolveCaller() {
        caller ?: return
        SymbolResolver.createSolverNode(caller!!, symbolMap)
            ?.let {
                callerResolverNode = it
                it.resolve()
            }
    }
}