package second_pass.resolver

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.expr.*
import second_pass.hierarchy.HierarchyGraph
import second_pass.resolver.solver_nodes.abs.BaseSolverNode
import second_pass.resolver.solver_nodes.cadet.ConstructorSolverNode
import second_pass.resolver.solver_nodes.cadet.FieldAccessSolverNode
import second_pass.resolver.solver_nodes.cadet.MethodSolverNode
import second_pass.resolver.solver_nodes.cadet.NameSolverNode
import second_pass.resolver.solver_nodes.common.*

class SymbolResolver {

    companion object {
        const val WildcardType: String = "#"
    }

    private lateinit var wizard: ResolverWizard
    fun getWizard() = wizard;

    fun resolve(node: Node, wizard: ResolverWizard) {
        val sNode = createSolverNode(node)
        sNode ?: error("Unresolvable node type: ${node.metaModel.typeName}.")
        this.wizard = wizard;
        sNode.resolve()
    }

    fun createSolverNode(node: Node): BaseSolverNode? {
        return when (node) {
            is MethodCallExpr -> MethodSolverNode(node, this)
            is ObjectCreationExpr -> ConstructorSolverNode(node, this)

            is LiteralExpr -> LiteralSolverNode(node)
            is CastExpr -> CastSolverNode(node, this)
            is NullLiteralExpr -> NullSolverNode(node)

            is ThisExpr -> ThisSolverNode(node, this)
            is SuperExpr -> SuperSolverNode(node, this)

            is NameExpr -> NameSolverNode(node, this)
            is FieldAccessExpr -> FieldAccessSolverNode(node, this)

            is EnclosedExpr -> EnclosedExprSolverNode(node, this)
            else -> return null
        }
    }
}