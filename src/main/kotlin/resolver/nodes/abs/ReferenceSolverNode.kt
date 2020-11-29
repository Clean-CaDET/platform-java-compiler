package resolver.nodes.abs

import com.github.javaparser.ast.Node
import resolver.SymbolMap

abstract class ReferenceSolverNode(node: Node, protected val symbolMap: SymbolMap): BaseSolverNode(node) {

}