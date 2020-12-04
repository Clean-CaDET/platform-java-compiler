package resolver

import context.VisitorContext
import hierarchy.HierarchyGraph
import model.*
import model.abs.CadetVariable
import signature.MemberSignature
import java.lang.IllegalArgumentException

class SymbolContextMap(
    private val hierarchyGraph: HierarchyGraph,
    private val visitorContext: VisitorContext
) {
    fun createClassContext(cadetClass: CadetClass) = visitorContext.createClassContext(cadetClass)
    fun createMemberContext(signature: MemberSignature) = visitorContext.createMemberContext(signature)
    fun getMemberContext() = visitorContext.memberContext

    fun modifyCurrentClassHierarchy(symbolName: String) {
        hierarchyGraph.modifyClassHierarchy(currentClass().name, symbolName)
    }

    fun getMethod(callerType: String?, signature: MemberSignature): CadetMember? {
        callerType ?: return getMemberFromHierarchy(signature, currentClass().name)

        getClass(callerType).let { Class ->
            Class ?: return null
            return getMemberFromHierarchy(signature, Class.name)
        }
    }

    fun getConstructor(className: String, signature: MemberSignature): CadetMember? {
        getClass(className)?.let { return it.getMemberViaSignature(signature) }
        return null
    }

    fun getField(callerType: String, variableName: String): CadetField? {
        getClassHierarchy(callerType)
            .forEach { Class ->
                Class.getField(variableName)?.let { return it }
            }
        return null
    }

    fun getVariableInContext(name: String): CadetVariable? {
        visitorContext.getMemberContextScopedVariable(name)?.let { return it }
        return getField(currentClass().name, name)
    }

    fun <T> notifyUsage(resolvedReference: T) {
        when (resolvedReference) {
            is CadetMember -> visitorContext.addMemberInvocation(resolvedReference)
            is CadetField -> visitorContext.addFieldAccess(resolvedReference)
            is CadetParameter -> {}
            is CadetLocalVariable -> {}
            else -> throw IllegalArgumentException("Unsupported reference usage: ${resolvedReference.toString()}")
        }
    }

    fun getContextClassSuperType() = hierarchyGraph.getClassParent(currentClass().name)?.name
    fun getContextClassName() = currentClass().name

    private fun currentClass() = visitorContext.classContext.cadetClass
    private fun getClass(name: String) = hierarchyGraph.getClass(name)
    private fun getClassHierarchy(className: String) = hierarchyGraph.getClassHierarchy(className)

    private fun getMemberFromHierarchy(signature: MemberSignature, className: String): CadetMember? {
        getClassHierarchy(className).forEach { classObj ->
            classObj.getMemberViaSignature(signature)?.let { return it }
        }
        return null
    }
}