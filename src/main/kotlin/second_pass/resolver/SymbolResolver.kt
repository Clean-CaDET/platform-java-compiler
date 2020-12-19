package second_pass.resolver

import cadet_model.CadetField
import cadet_model.CadetMember
import cadet_model.abs.CadetVariable
import com.github.javaparser.ast.Node
import com.github.javaparser.ast.expr.*
import second_pass.context.VisitorContext
import second_pass.hierarchy.HierarchyGraph
import second_pass.resolver.solver_nodes.abs.BaseSolverNode
import second_pass.resolver.solver_nodes.cadet.ConstructorSolverNode
import second_pass.resolver.solver_nodes.cadet.FieldAccessSolverNode
import second_pass.resolver.solver_nodes.cadet.MethodSolverNode
import second_pass.resolver.solver_nodes.cadet.NameSolverNode
import second_pass.resolver.solver_nodes.common.*
import second_pass.signature.MemberSignature

class SymbolResolver(
    private val visitorContext: VisitorContext,
    private val hierarchyGraph: HierarchyGraph
) {
    companion object {
        const val WildcardType: String = "#"
    }

    fun resolve(node: Node) {
        val sNode = createSolverNode(node)
        sNode ?: throw IllegalArgumentException("Unresolvable node type: ${node.metaModel.typeName}.")
        sNode.resolve()
    }

    fun getVisitorContext() = visitorContext
    fun getHierarchyGraph() = hierarchyGraph

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

            else -> return null
        }
    }

    fun getConstructor(className: String, signature: MemberSignature): CadetMember? {
        hierarchyGraph.getClass(className)?.let { return it.getMemberViaSignature(signature) }
        return null
    }

    fun getField(callerType: String, variableName: String): CadetField? {
        hierarchyGraph.getClassHierarchy(callerType)
            .forEach { Class ->
                Class.getField(variableName)?.let { return it }
            }
        return null
    }

    fun getVariableInScope(name: String): CadetVariable? {
        visitorContext.getMemberScopedVariable(name)?.let { return it }
        return getField(visitorContext.getCurrentClassName(), name)
    }

    fun getMethod(callerType: String?, signature: MemberSignature): CadetMember? {
        callerType ?: return getMemberFromHierarchy(signature, visitorContext.getCurrentClassName())

        hierarchyGraph.getClass(callerType).let { Class ->
            Class ?: return null
            return getMemberFromHierarchy(signature, Class.name)
        }
    }

    fun getCurrentClassSuperType(): String? {
        hierarchyGraph.getClassParent(visitorContext.getCurrentClassName())?.let { return it.name }
        return null
    }

    private fun getMemberFromHierarchy(signature: MemberSignature, className: String): CadetMember? {
        hierarchyGraph.getClassHierarchy(className).forEach { classObj ->
            classObj.getMemberViaSignature(signature)?.let { return it }
        }
        return null
    }
}