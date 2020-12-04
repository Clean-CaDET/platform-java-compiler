package second_pass.resolver.solver_nodes.abs

import com.github.javaparser.ast.Node

abstract class BaseSolverNode(protected val node: Node) {
    lateinit var returnType: String
    abstract fun resolve()
}