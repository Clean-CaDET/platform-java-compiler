package context

import model.*
import signature.MemberSignature

class MemberContext(
    classContext: ClassContext,
    signature: MemberSignature
) : ClassContext(classContext.cadetClass) {

    private val cadetMember: CadetMember

    init {
        cadetClass.getMemberViaSignature(signature)
        .let {
            it ?: throw IllegalArgumentException("Failed to create member context in class ${classContext.cadetClass.name}")
            this.cadetMember = it
        }
    }

    fun addInvokedMember(member: CadetMember) = cadetMember.invokedMethods.add(member)
    fun addLocalVariable(localVariable: CadetLocalVariable) = cadetMember.localVariables.add(localVariable)
    fun addAccessedField(field: CadetField) = cadetMember.accessedFields.add(field)

    fun getParameter(name: String) = cadetMember.params.find { it.name == name }
    fun getLocalVariable(name: String) = cadetMember.localVariables.find { it.name == name }
}