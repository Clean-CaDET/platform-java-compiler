package resolver

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.expr.*
import resolver.nodes.abs.BaseSolverNode
import resolver.nodes.cadet.ConstructorSolverNode
import resolver.nodes.cadet.FieldAccessSolverNode
import resolver.nodes.cadet.MethodSolverNode
import resolver.nodes.cadet.NameSolverNode
import resolver.nodes.common.*
import java.lang.IllegalArgumentException

class SymbolResolver(private val symbolSolvingBundle: SymbolSolvingBundle) {

    fun resolve(node: Node) {
        val sNode = createSolverNode(node, symbolSolvingBundle)
        sNode ?: throw IllegalArgumentException("Unresolvable node type: ${node.metaModel.typeName}.")
        sNode.resolve()
    }

    companion object {
        const val WildcardType: String = "#"

        fun createSolverNode(node: Node, symbolSolvingBundle: SymbolSolvingBundle): BaseSolverNode? {
            return when (node) {
                is MethodCallExpr -> MethodSolverNode(node, symbolSolvingBundle)
                is ObjectCreationExpr -> ConstructorSolverNode(node, symbolSolvingBundle)

                is LiteralExpr -> LiteralSolverNode(node)
                is CastExpr -> CastSolverNode(node, symbolSolvingBundle)
                is NullLiteralExpr -> NullSolverNode(node)

                is ThisExpr -> ThisSolverNode(node, symbolSolvingBundle)
                is SuperExpr -> SuperSolverNode(node, symbolSolvingBundle)

                is NameExpr -> NameSolverNode(node, symbolSolvingBundle)
                is FieldAccessExpr -> FieldAccessSolverNode(node, symbolSolvingBundle)

                else -> return null
            }
        }
    }
}