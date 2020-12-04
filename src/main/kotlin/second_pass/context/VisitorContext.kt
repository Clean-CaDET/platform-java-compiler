package second_pass.context

import cadet_model.*
import cadet_model.abs.CadetVariable
import second_pass.signature.MemberSignature
import java.lang.IllegalArgumentException

class VisitorContext {
    lateinit var classContext: ClassContext
    lateinit var memberContext: MemberContext

    fun createClassContext(cadetClass: CadetClass) {
        classContext = ClassContext(cadetClass)
    }

    fun getCurrentClassName() = classContext.cadetClass.name

    fun createMemberContext(signature: MemberSignature) {
        memberContext = MemberContext(classContext, signature)
    }

    private fun addMemberInvocation(cadetMember: CadetMember) = memberContext.addInvokedMember(cadetMember)
    private fun addFieldAccess(cadetField: CadetField) = memberContext.addAccessedField(cadetField)

    fun getMemberContextScopedVariable(name: String): CadetVariable? {
        memberContext.getParameter(name)?.let { return it }
        memberContext.getLocalVariable(name)?.let { return it }
        return null
    }
    fun <T> notifyUsage(resolvedReference: T) {
        when (resolvedReference) {
            is CadetMember -> addMemberInvocation(resolvedReference)
            is CadetField -> addFieldAccess(resolvedReference)
            is CadetParameter -> {}
            is CadetLocalVariable -> {}
            else -> throw IllegalArgumentException("Unsupported reference usage: ${resolvedReference.toString()}")
        }
    }

}