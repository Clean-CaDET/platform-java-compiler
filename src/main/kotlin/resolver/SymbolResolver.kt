package resolver

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.expr.*
import model.CadetMember
import resolver.nodes.abs.BaseSolverNode
import resolver.nodes.cadet.ConstructorSolverNode
import resolver.nodes.cadet.FieldAccessSolverNode
import resolver.nodes.cadet.MethodSolverNode
import resolver.nodes.cadet.NameSolverNode
import resolver.nodes.common.*
import java.lang.IllegalArgumentException

class SymbolResolver(private val symbolMap: SymbolContextMap) {

    fun resolve(node: Node) {
        val sNode = createSolverNode(node, symbolMap)
        sNode ?: throw IllegalArgumentException("Unresolvable node type: ${node.metaModel.typeName}.")
        sNode.resolve()
    }

    companion object {
        const val WildcardType: String = "#"

        fun createSolverNode(node: Node, symbolMap: SymbolContextMap): BaseSolverNode? {
            return when (node) {
                is MethodCallExpr -> MethodSolverNode(node, symbolMap)
                is ObjectCreationExpr -> ConstructorSolverNode(node, symbolMap)

                is LiteralExpr -> LiteralSolverNode(node)
                is ThisExpr -> ThisSolverNode(node, symbolMap)
                is CastExpr -> CastSolverNode(node, symbolMap)
                is NullLiteralExpr -> NullSolverNode(node)


                is SuperExpr -> SuperSolverNode(node, symbolMap)

                is NameExpr -> NameSolverNode(node, symbolMap)
                is FieldAccessExpr -> FieldAccessSolverNode(node, symbolMap)

                else -> return null
            }
        }
    }
}