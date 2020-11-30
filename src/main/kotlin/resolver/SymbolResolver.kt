package resolver

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.expr.*
import model.CadetMember
import resolver.nodes.abs.BaseSolverNode
import resolver.nodes.cadet.ConstructorSolverNode
import resolver.nodes.cadet.MethodSolverNode
import resolver.nodes.common.*
import java.lang.IllegalArgumentException

class SymbolResolver(private val symbolMap: SymbolContextMap) {

    fun resolve(node: MethodCallExpr): CadetMember? {
        val sNode = MethodSolverNode(node, symbolMap)
        sNode.resolve()
        return try {
            sNode.getResult()
            .apply {
                println("\tResolved: '${this.name}()' from '${this.parent.name}'")
            }
        }
        catch (e: IllegalAccessError) {
            println("Failed to resolve '${node.nameAsString}()' from ${symbolMap.getContextClassName()}")
            null
        }
    }

    companion object {
        const val WildcardType: String = "#"

        fun createSolverNode(node: Node, symbolMap: SymbolContextMap): BaseSolverNode? {
            return when (node) {
                is MethodCallExpr -> MethodSolverNode(node, symbolMap)
                is ObjectCreationExpr -> ConstructorSolverNode(node, symbolMap)

                is LiteralExpr -> LiteralSolverNode(node)
                is ThisExpr -> ThisSolverNode(node, symbolMap)
                is CastExpr -> CastSolverNode(node)

                is SuperExpr -> SuperSolverNode(node, symbolMap)

                is NameExpr -> NameExpressionSolverNode(node, symbolMap)
                is FieldAccessExpr -> throw IllegalArgumentException("Field access not supported by resolver.")

                else -> return null
            }
        }
    }
}