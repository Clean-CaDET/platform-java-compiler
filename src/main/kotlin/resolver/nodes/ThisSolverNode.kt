package resolver.nodes

import resolver.SymbolMap
import resolver.nodes.abs.BaseSolverNode

internal class ThisSolverNode(symbolMap: SymbolMap) : BaseSolverNode() {

    init {
        this.returnType = symbolMap.getCurrentClassName()
    }
}