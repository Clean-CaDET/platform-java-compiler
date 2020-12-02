package resolver

import context.ContextHolder
import hierarchy.InheritanceGraph
import model.*
import model.abs.CadetVariable
import signature.MemberSignature
import java.lang.IllegalArgumentException

class SymbolContextMap {
    private val classes = mutableListOf<CadetClass>()
    private val hierarchyGraph = InheritanceGraph()
    private val contextHolder = ContextHolder()

    fun printClassHierarchy() {
        hierarchyGraph.printHierarchy()
    }

    fun getClassAt(index: Int) = classes[index]
    fun getClasses(): List<CadetClass> = classes
    fun addClass(cadetClass: CadetClass) {
        classes.add(cadetClass)
        hierarchyGraph.addClass(cadetClass)
    }

    fun createClassContext(cadetClass: CadetClass) = contextHolder.createClassContext(cadetClass)
    fun createMemberContext(signature: MemberSignature) = contextHolder.createMemberContext(signature)
    fun getMemberContext() = contextHolder.memberContext

    fun modifyCurrentClassHierarchy(superClassName: String) {
        hierarchyGraph.modifyClassParent(currentClass().name, superClassName)
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
            is CadetMember -> {
                contextHolder.addMemberInvocation(resolvedReference)
                //println("\tResolved method: ${resolvedReference.returnType} ${resolvedReference.name}()")
            }
            is CadetField -> {
                contextHolder.addFieldAccess(resolvedReference)
                //println("\tResolved field: ${resolvedReference.type} ${resolvedReference.name}")
            }
            is CadetParameter -> {
                //println("\tResolved parameter: ${resolvedReference.type} ${resolvedReference.name}")
            }
            is CadetLocalVariable -> {
                //println("\tResolved local variable ${resolvedReference.type} ${resolvedReference.name}")
            }
            else -> throw IllegalArgumentException("Unsupported reference usage: ${resolvedReference.toString()}")
        }
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