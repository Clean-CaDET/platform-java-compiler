package resolver.nodes.abs

import com.github.javaparser.ast.Node

abstract class ReferenceSolverNode(protected val node: Node) : BaseSolverNode() {

    abstract fun resolve()
}