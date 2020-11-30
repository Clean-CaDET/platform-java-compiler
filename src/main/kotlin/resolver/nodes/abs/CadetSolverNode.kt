package resolver.nodes.abs

import com.github.javaparser.ast.Node
import resolver.SymbolContextMap

abstract class CadetSolverNode<T>(node: Node, symbolMap: SymbolContextMap) : ReferenceSolverNode(node, symbolMap) {

    protected abstract var caller: Node?
    protected var callerResolverNode: BaseSolverNode? = null

    protected var resolvedReference: T? = null

    fun notifyContextOfUsage() {
        resolvedReference ?: return
        symbolMap.notifyUsage(resolvedReference)
        resolvedReference = null
    }
}