package second_pass.resolver.solver_nodes.common

import com.github.javaparser.ast.Node
import second_pass.resolver.SymbolSolvingBundle
import second_pass.resolver.solver_nodes.abs.BaseSolverNode

class ThisSolverNode(node: Node, private val symbolSolvingBundle: SymbolSolvingBundle) : BaseSolverNode(node) {

    override fun resolve() {
        this.returnType = symbolSolvingBundle.getVisitorContext().getCurrentClassName()
    }
}