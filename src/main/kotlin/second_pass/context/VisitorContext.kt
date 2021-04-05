package second_pass.context

import cadet_model.*
import cadet_model.abs.CadetVariable
import prototype_dto.ClassPrototype
import second_pass.signature.MemberSignature

class VisitorContext {
    lateinit var classContext: ClassContext
    lateinit var memberContext: MemberContext
    private var isInMember = false

    fun createClassContext(cadetClass: CadetClass) {
        println("[CC] ${cadetClass.fullName}")
        classContext = ClassContext(cadetClass)
    }

    fun getCurrentClassName() = classContext.cadetClass.name

    fun createMemberContext(signature: MemberSignature) {
        println("[MC] $signature")
        memberContext = MemberContext(classContext, signature)
        isInMember = true
    }

    fun removeMemberContext() {
        isInMember = false
    }

    fun hasMemberContext(): Boolean = isInMember

    private fun addMemberInvocation(cadetMember: CadetMember) = memberContext.addInvokedMember(cadetMember)
    private fun addFieldAccess(cadetField: CadetField) = memberContext.addAccessedField(cadetField)

    fun getMemberScopedVariable(name: String): CadetVariable? {
        memberContext.getParameter(name)?.let { return it }
        memberContext.getLocalVariable(name)?.let { return it }
        return null
    }

    fun <T : Any> notifyUsage(resolvedReference: T) {
        when (resolvedReference) {
            is CadetMember -> addMemberInvocation(resolvedReference)
            is CadetField -> addFieldAccess(resolvedReference)
            is CadetParameter -> {}
            is CadetLocalVariable -> {}
            else -> throw IllegalArgumentException("Unsupported reference usage: $resolvedReference")
        }
    }
}