package resolver.nodes.common

import com.github.javaparser.ast.Node
import resolver.SymbolContextMap
import resolver.nodes.abs.ReferenceSolverNode

class ThisSolverNode(node: Node, symbolMap: SymbolContextMap) : ReferenceSolverNode(node, symbolMap) {

    override fun resolve() {
        this.returnType = symbolMap.getContextClassName()
    }
}