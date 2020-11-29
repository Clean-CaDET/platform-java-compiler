package resolver.nodes.abs

import com.github.javaparser.ast.Node

internal abstract class ReferenceSolverNode(protected val node: Node) : BaseSolverNode() {

    abstract fun resolve()
}