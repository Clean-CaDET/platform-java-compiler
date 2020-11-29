package resolver.nodes

import resolver.SymbolMap
import resolver.nodes.abs.BaseSolverNode

class ThisSolverNode(symbolMap: SymbolMap) : BaseSolverNode() {

    init {
        this.returnType = symbolMap.getCurrentClassName()
    }
}