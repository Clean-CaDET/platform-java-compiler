package resolver

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.expr.*
import model.CadetMember
import resolver.nodes.*
import resolver.nodes.abs.BaseSolverNode
import java.lang.IllegalArgumentException

class SymbolResolver(private val symbolMap: SymbolMap) {

    fun resolve(node: MethodCallExpr): CadetMember? {
        val sNode = MethodSolverNode(node, symbolMap)
        sNode.resolve()
        return try {
            sNode.getResult()
            .apply {
                println("Resolved: '${this.returnType} ${this.name}()' from '${this.parent.name}'")
            }
        }
        catch (e: IllegalAccessError) {
            println("Failed to resolve '${node.nameAsString}()' from ${symbolMap.getContextClassName()}")
            //Console.printTree(node)
            null
        }
    }

    companion object {
        const val WildcardParameter: String = "#"

        fun createSolverNode(node: Node, symbolMap: SymbolMap): BaseSolverNode? {
            return when (node) {
                is MethodCallExpr -> MethodSolverNode(node, symbolMap)
                is ObjectCreationExpr -> ConstructorSolverNode(node, symbolMap)

                is LiteralExpr -> LiteralSolverNode(node)
                is ThisExpr -> ThisSolverNode(node, symbolMap)
                is CastExpr -> CastSolverNode(node)

                is NameExpr -> NameExpressionSolverNode(node, symbolMap)
                is FieldAccessExpr -> throw IllegalArgumentException("Field access not supported by resolver.")

                else -> return null
            }
        }
    }
}