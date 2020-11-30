package visitor

import context.ContextHolder
import hierarchy.InheritanceGraph
import model.CadetClass
import model.CadetField
import model.CadetMember
import model.interfaces.CadetVariable
import resolver.SymbolContextMap
import signature.MemberSignature
import java.lang.IllegalArgumentException

class VisitorClassMap : SymbolContextMap {
    private val classes = mutableListOf<CadetClass>()
    private val hierarchyGraph = InheritanceGraph()
    private val contextHolder = ContextHolder()

    override fun getClassAt(index: Int) = classes[index]
    override fun getClasses(): List<CadetClass> = classes
    override fun addClass(cadetClass: CadetClass) {
        classes.add(cadetClass)
        hierarchyGraph.addClass(cadetClass)
    }

    fun createClassContext(cadetClass: CadetClass) = contextHolder.createClassContext(cadetClass)
    fun createMemberContext(signature: MemberSignature) = contextHolder.createMemberContext(signature)
    fun getMemberContext() = contextHolder.memberContext

    override fun modifyCurrentClassParent(superClassName: String) {
        hierarchyGraph.modifyClassParent(currentClass().name, superClassName)
    }

    override fun getCadetMemberInContext(callerName: String?, signature: MemberSignature): CadetMember? {
        if (callerName == null || belongsToClassHierarchy(callerName, currentClass().name)) {
            return getCadetMember(signature, getClassHierarchy(currentClass().name))
        }
        contextHolder.getContextScopedType(callerName)
            ?.let { callerType ->
                return getCadetMember(signature, getClassHierarchy(callerType))
            }
        getClass(callerName)?.let {
            return getCadetMember(signature, getClassHierarchy(it.name))
        }
        return null
    }

    override fun getCadetVariableInContext(name: String): CadetVariable? {
        contextHolder.getMemberContextScopedVariable(name)?.let { return it }
        getClassHierarchy(currentClass().name)
            .forEach { Class ->
                Class.getField(name)?.let { return it }
            }
        return null
    }

    override fun <T> notifyUsage(resolvedReference: T) {
        when (resolvedReference) {
            is CadetMember -> contextHolder.addMemberInvocation(resolvedReference)
            is CadetField -> contextHolder.addFieldAccess(resolvedReference)
            else -> throw IllegalArgumentException("Unsupported reference usage: ${resolvedReference.toString()}")
        }
    }

    override fun getContextClassSuperType(): String? = hierarchyGraph.getClassParent(currentClass().name)?.name
    override fun getContextClassName(): String = currentClass().name
    private fun currentClass() = contextHolder.classContext.cadetClass
    private fun getClass(name: String): CadetClass? = classes.find { Class -> Class.name == name }
    private fun getClassHierarchy(className: String) = hierarchyGraph.getClassHierarchy(className)
    private fun belongsToClassHierarchy(name: String, className: String): Boolean
            = hierarchyGraph.belongsToClassHierarchy(name, className)

    private fun getCadetMember(signature: MemberSignature, classList: List<CadetClass>): CadetMember? {
        classList.forEach { classObj ->
            classObj.getMemberViaSignature(signature)?.let { return it }
        }
        return null
    }
}