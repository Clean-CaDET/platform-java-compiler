package second_pass.resolver.solver_nodes.common

import com.github.javaparser.ast.Node
import second_pass.resolver.SymbolResolver
import second_pass.resolver.solver_nodes.abs.BaseSolverNode

class ThisSolverNode(node: Node, private val resolver: SymbolResolver) : BaseSolverNode(node) {

    override fun resolve() {
        this.returnType = resolver.getVisitorContext().getCurrentClassName()
    }
}