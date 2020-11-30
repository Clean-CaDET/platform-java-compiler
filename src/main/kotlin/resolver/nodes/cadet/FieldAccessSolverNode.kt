package resolver.nodes.cadet

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.expr.FieldAccessExpr
import model.CadetField
import resolver.SymbolContextMap
import resolver.nodes.abs.CadetSolverNode

class FieldAccessSolverNode(node: FieldAccessExpr, symbolMap: SymbolContextMap): CadetSolverNode<CadetField>(node, symbolMap) {

    override var caller: Node? = null

    override fun doResolve() {

    }
}