package resolver

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.expr.*
import model.CadetMember
import parser.node.MethodCallExpressionParser
import resolver.nodes.ConstructorSolverNode
import resolver.nodes.LiteralSolverNode
import resolver.nodes.MethodSolverNode
import resolver.nodes.ThisSolverNode
import resolver.nodes.abs.BaseSolverNode
import resolver.nodes.abs.CallSolverNode
import signature.MemberSignature
import java.lang.IllegalArgumentException

class SymbolResolver(private val symbolMap: SymbolMap) {

    fun resolve(node: MethodCallExpr, caller: Pair<Node, String?>?): CadetMember? {
        MethodSolverNode(node, symbolMap)
            .also { sNode ->
                sNode.resolve()
                sNode.getResolvedReference()?.let {
                    println("Resolved ${it.returnType} ${it.name}()")
                    return it
                }
                println("Failed to resolve '${node.nameAsString}()'")
                return null
            }
    }

    companion object {
        fun createSolverNode(node: Node, symbolMap: SymbolMap): BaseSolverNode? {
            return when (node) {
                is MethodCallExpr -> MethodSolverNode(node, symbolMap)
                is ObjectCreationExpr -> ConstructorSolverNode(node, symbolMap)
                is LiteralExpr -> LiteralSolverNode(node)
                is ThisExpr -> ThisSolverNode(symbolMap)
                is FieldAccessExpr -> throw IllegalArgumentException("Field access not supported by resolver.")
                else -> return null
            }
        }
    }
}