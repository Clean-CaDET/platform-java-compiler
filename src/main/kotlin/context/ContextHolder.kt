package context

import model.CadetClass
import model.CadetField
import model.CadetMember
import model.abs.CadetVariable
import signature.MemberSignature

class ContextHolder {
    lateinit var classContext: ClassContext
    lateinit var memberContext: MemberContext

    fun createClassContext(cadetClass: CadetClass) {
        classContext = ClassContext(cadetClass)
    }

    fun createMemberContext(signature: MemberSignature) {
        memberContext = MemberContext(classContext, signature)
    }

    fun addMemberInvocation(cadetMember: CadetMember) = memberContext.addInvokedMember(cadetMember)
    fun addFieldAccess(cadetField: CadetField) = memberContext.addAccessedField(cadetField)

    /** @return Type name if the given caller exists within the current member or class context */
    fun getContextScopedType(callerName: String): String? {
        getMemberContextScopedType(callerName)?.let { return it }
        getClassContextScopedType(callerName)?.let { return it }

        return null
    }

    /** @return Type name if the given caller name is a member parameter or local variable */
    fun getMemberContextScopedType(callerName: String): String? {
        memberContext.getContextScopedLocalVariable(callerName)?.let { return it.type }
        memberContext.getContextScopedParameter(callerName)?.let { return it.type }
        return null
    }

    /** @return Type name if the given caller name is a class field */
    fun getClassContextScopedType(callerName: String): String? {
        classContext.getContextScopedField(callerName)?.let { return it.type }
        return null
    }

    fun getMemberContextScopedVariable(name: String): CadetVariable? {
        memberContext.getContextScopedParameter(name)?.let { return it }
        memberContext.getContextScopedLocalVariable(name)?.let { return it }
        return null
    }
}