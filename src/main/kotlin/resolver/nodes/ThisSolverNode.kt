package resolver.nodes

import com.github.javaparser.ast.Node
import resolver.SymbolMap
import resolver.nodes.abs.ReferenceSolverNode

class ThisSolverNode(node: Node, symbolMap: SymbolMap) : ReferenceSolverNode(node, symbolMap) {

    override fun resolve() {
        this.returnType = symbolMap.getContextClassName()
    }
}