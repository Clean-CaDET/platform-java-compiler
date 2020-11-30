package resolver.nodes.abs

import com.github.javaparser.ast.Node
import resolver.SymbolContextMap

abstract class ReferenceSolverNode(node: Node, protected val symbolMap: SymbolContextMap): BaseSolverNode(node) {

}