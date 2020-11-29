package context

import model.*
import signature.SignableCadetMember
import signature.MemberSignature

class MemberContext(
    classContext: Context,
    signature: MemberSignature
) : Context(classContext.cadetClass) {

    private val cadetMember: CadetMember

    init {
        cadetClass.members.find {
            signature.compareTo(SignableCadetMember(it))
        }.also {
            it ?: throw IllegalArgumentException("Cannot create method context.")
            this.cadetMember = it
        }
    }

    fun addInvokedMember(member: CadetMember) {
        cadetMember.invokedMethods.add(member)
    }

    fun addLocalVariable(localVariable: CadetLocalVariable) {
        cadetMember.localVariables.add(localVariable)
    }

    fun addAccessedField(field: CadetField) {
        cadetMember.accessedFields.add(field)
    }

    fun getContextScopedParameter(name: String): CadetParameter? {
        return cadetMember.params.find { param ->
            param.name == name
        }
    }

    fun getContextScopedLocalVariable(name: String): CadetLocalVariable? {
        return cadetMember.localVariables.find { field ->
            field.name == name
        }
    }
}