package resolver.nodes.common

import com.github.javaparser.ast.Node
import context.VisitorContext
import resolver.SymbolSolvingBundle
import resolver.nodes.abs.BaseSolverNode

class ThisSolverNode(node: Node, private val symbolSolvingBundle: SymbolSolvingBundle) : BaseSolverNode(node) {

    override fun resolve() {
        this.returnType = symbolSolvingBundle.getVisitorContext().getCurrentClassName()
    }
}