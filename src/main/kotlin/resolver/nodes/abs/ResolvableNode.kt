package resolver.nodes.abs

import com.github.javaparser.ast.Node

abstract class ResolvableNode(protected val node: Node) : BaseSolverNode() {

    abstract fun resolve()
}