package second_pass.resolver

import cadet_model.CadetField
import cadet_model.CadetLocalVariable
import cadet_model.CadetMember
import cadet_model.abs.CadetVariable
import second_pass.hierarchy.HierarchyGraph
import second_pass.signature.MemberSignature

class ScopeContext(
    private val hierarchyGraph: HierarchyGraph,
    private val cadetMember: CadetMember,
    private val localVariables: List<CadetLocalVariable>
){
    fun getConstructor(signature: MemberSignature): CadetMember? {
        hierarchyGraph.getClass(signature.getMemberName())?.let {
            return it.getMemberViaSignature(signature.withHierarchyGraph(hierarchyGraph))
        }
        return null
    }

    fun getField(callerType: String, variableName: String): CadetField? {
        for (c in hierarchyGraph.getClassHierarchy(callerType))
            c.getField(variableName)?.let { return it }
        return null
    }

    fun getVariableInScope(name: String): CadetVariable? {
        localVariables.find { variable -> variable.name == name }?.let { return it }
        cadetMember.params.find { param -> param.name == name }?.let { return it }
        cadetMember.parent.fields.find { field -> field.name == name}?.let { return it }
        return null
    }

    fun getMethod(signature: MemberSignature, callerType: String? = null): CadetMember? {
        callerType ?: return getMemberFromHierarchy(signature, cadetMember.parent.name)

        hierarchyGraph.getClass(callerType).let { cadetClass ->
            cadetClass ?: return null
            return getMemberFromHierarchy(signature, cadetClass.name)
        }
    }

    private fun getMemberFromHierarchy(signature: MemberSignature, className: String): CadetMember? {
        hierarchyGraph.getClassHierarchy(className).forEach { c ->
            c.getMemberViaSignature(signature.withHierarchyGraph(hierarchyGraph))
                ?.let { foundMember -> return foundMember }
        }
        return null
    }

    fun getCurrentClassSuperType(): String? {
        return cadetMember.parent.parent?.name
    }

    fun getCurrentClassName(): String {
        return cadetMember.parent.name
    }
}