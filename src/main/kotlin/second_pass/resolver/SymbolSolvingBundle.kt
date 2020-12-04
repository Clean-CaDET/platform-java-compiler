package second_pass.resolver

import second_pass.context.VisitorContext
import second_pass.hierarchy.HierarchyGraph
import cadet_model.*
import cadet_model.abs.CadetVariable
import second_pass.signature.MemberSignature

class SymbolSolvingBundle(
    private val hierarchyGraph: HierarchyGraph,
    private val visitorContext: VisitorContext
) {
    fun getVisitorContext() = visitorContext

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

    fun getVariableInContext(name: String): CadetVariable? {
        visitorContext.getMemberContextScopedVariable(name)?.let { return it }
        return getField(visitorContext.getCurrentClassName(), name)
    }

    fun getCurrentClassSuperType(): String? {
        hierarchyGraph.getClassParent(visitorContext.getCurrentClassName())?.let { return it.name }
        return null
    }

    fun getMethod(callerType: String?, signature: MemberSignature): CadetMember? {
        callerType ?: return getMemberFromHierarchy(signature, visitorContext.getCurrentClassName())

        hierarchyGraph.getClass(callerType).let { Class ->
            Class ?: return null
            return getMemberFromHierarchy(signature, Class.name)
        }
    }

    private fun getMemberFromHierarchy(signature: MemberSignature, className: String): CadetMember? {
        hierarchyGraph.getClassHierarchy(className).forEach { classObj ->
            classObj.getMemberViaSignature(signature)?.let { return it }
        }
        return null
    }
}