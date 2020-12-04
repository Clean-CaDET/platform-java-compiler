package context

import model.CadetClass
import model.CadetField
import model.CadetMember
import model.abs.CadetVariable
import signature.MemberSignature

class VisitorContext {
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

    fun getMemberContextScopedVariable(name: String): CadetVariable? {
        memberContext.getParameter(name)?.let { return it }
        memberContext.getLocalVariable(name)?.let { return it }
        return null
    }
}