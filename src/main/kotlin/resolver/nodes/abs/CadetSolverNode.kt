package resolver.nodes.abs

import com.github.javaparser.ast.Node
import resolver.SymbolContextMap

abstract class CadetSolverNode<T>(node: Node, symbolMap: SymbolContextMap) : ReferenceSolverNode(node, symbolMap) {

    protected var resolvedReference: T? = null

    private fun notifyContextOfUsage() {
        if (resolvedReference == null) return
        symbolMap.notifyUsage(resolvedReference)
    }

    protected abstract fun doResolve()

    override fun resolve() {
        doResolve()
        notifyContextOfUsage()
    }
}