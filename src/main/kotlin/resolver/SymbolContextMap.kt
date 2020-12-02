package resolver

import context.ContextHolder
import hierarchy.HierarchyGraph
import model.*
import model.abs.CadetVariable
import signature.MemberSignature
import java.lang.IllegalArgumentException

class SymbolContextMap {
    private val classes = mutableListOf<CadetClass>()
    private val hierarchyGraph = HierarchyGraph()
    private val contextHolder = ContextHolder()

    fun printClassHierarchy() = hierarchyGraph.printHierarchy()

    fun getClassAt(index: Int) = classes[index]
    fun getClasses(): List<CadetClass> = classes
    fun addClass(cadetClass: CadetClass) {
        classes.add(cadetClass)
        hierarchyGraph.addClass(cadetClass)
    }
    fun addInterface(name: String) = hierarchyGraph.addInterface(name)

    fun createClassContext(cadetClass: CadetClass) = contextHolder.createClassContext(cadetClass)
    fun createMemberContext(signature: MemberSignature) = contextHolder.createMemberContext(signature)
    fun getMemberContext() = contextHolder.memberContext

    fun modifyCurrentClassHierarchy(symbolName: String) {
        hierarchyGraph.modifyClassHierarchy(currentClass().name, symbolName)
    }

    fun getMember(callerType: String?, signature: MemberSignature): CadetMember? {
        if (callerType == null) {
            return getMemberFromHierarchy(signature, currentClass().name)
        }
        getClass(callerType)?.let {
            return getMemberFromHierarchy(signature, it.name)
        }
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
        contextHolder.getMemberContextScopedVariable(name)?.let { return it }
        getClassHierarchy(currentClass().name)
            .forEach { Class ->
                Class.getField(name)?.let { return it }
            }
        return null
    }

    fun <T> notifyUsage(resolvedReference: T) {
        when (resolvedReference) {
            is CadetMember -> contextHolder.addMemberInvocation(resolvedReference)
            is CadetField -> contextHolder.addFieldAccess(resolvedReference)
            is CadetParameter -> {
                //println("\tResolved parameter: ${resolvedReference.type} ${resolvedReference.name}")
            }
            is CadetLocalVariable -> {
                //println("\tResolved local variable ${resolvedReference.type} ${resolvedReference.name}")
            }
            else -> throw IllegalArgumentException("Unsupported reference usage: ${resolvedReference.toString()}")
        }
    }

    fun isSuperType(childType: String, parentType: String): Boolean {
        return hierarchyGraph.isSuperType(childType, parentType)
    }

    fun containsInterface(className: String, interfaceName: String): Boolean {
        return hierarchyGraph.containsInterface(className, interfaceName)
    }

    fun getContextClassSuperType(): String? = hierarchyGraph.getClassParent(currentClass().name)?.name
    fun getContextClassName(): String = currentClass().name

    private fun currentClass() = contextHolder.classContext.cadetClass
    private fun getClass(name: String): CadetClass? = hierarchyGraph.getClass(name)
    private fun getClassHierarchy(className: String) = hierarchyGraph.getClassHierarchy(className)

    private fun getMemberFromHierarchy(signature: MemberSignature, className: String): CadetMember? {
        getClassHierarchy(className).forEach { classObj ->
            classObj.getMemberViaSignature(signature)?.let { return it }
        }
        return null
    }
}