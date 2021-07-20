package second_pass.resolver

import cadet_model.CadetField
import cadet_model.CadetLocalVariable
import cadet_model.CadetMember
import cadet_model.abs.CadetVariable
import com.github.javaparser.ast.Node
import com.github.javaparser.ast.expr.*
import second_pass.infrastructure.hierarchy.HierarchyGraph
import second_pass.infrastructure.dto.ResolverContextData
import second_pass.resolver.solver_nodes.abs.BaseSolverNode
import second_pass.resolver.solver_nodes.cadet.ConstructorSolverNode
import second_pass.resolver.solver_nodes.cadet.FieldAccessSolverNode
import second_pass.resolver.solver_nodes.cadet.MethodSolverNode
import second_pass.resolver.solver_nodes.cadet.NameSolverNode
import second_pass.resolver.solver_nodes.common.*
import second_pass.signature.MemberSignature

class SymbolResolver(private val hierarchyGraph: HierarchyGraph) {

    companion object {
        const val WildcardType: String = "#"
    }

    // TODO These fields do not allow us to do multi-threaded resolving due to state-persistence
    private lateinit var currentCadetMember: CadetMember
    private lateinit var localVariables: List<CadetLocalVariable>

    fun resolve(context: ResolverContextData) {
        val sNode = createSolverNode(context.node)
        sNode ?: throw IllegalArgumentException("Unresolvable node type: ${context.node.metaModel.typeName}.")

        this.currentCadetMember = context.cadetMember
        this.localVariables = context.localVariables

        sNode.resolve()
    }

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

            is EnclosedExpr -> EnclosedExprSolverNode(node, this)
            else -> return null
        }
    }

    // TODO Everything below this point should NOT be inside of the resolver

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
        this.currentCadetMember.params.find { param -> param.name == name }?.let { return it }
        this.localVariables.find { variable -> variable.name == name }?.let { return it }
        this.currentCadetMember.parent.fields.find { field -> field.name == name}?.let { return it }

        return null
    }

    fun getMethod(callerType: String?, signature: MemberSignature): CadetMember? {
        callerType ?: return getMemberFromHierarchy(signature, this.currentCadetMember.parent.name)

        hierarchyGraph.getClass(callerType).let { cadetClass ->
            cadetClass ?: return null
            return getMemberFromHierarchy(signature, cadetClass.name)
        }
    }

    fun getCurrentClassSuperType(): String? {
        return this.currentCadetMember.parent.parent?.name
    }

    fun getCurrentClassName(): String {
        return this.currentCadetMember.parent.name
    }

    private fun getMemberFromHierarchy(signature: MemberSignature, className: String): CadetMember? {
        hierarchyGraph.getClassHierarchy(className).forEach { classObj ->
            classObj.getMemberViaSignature(signature)?.let { return it }
        }
        return null
    }
}