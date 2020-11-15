package context

import model.CadetClass
import model.CadetField
import model.CadetLocalVariable
import model.CadetMember
import signature.SignableCadetMember
import signature.MemberSignature

internal class MemberContext(
    cadetClass: CadetClass,
    signature: MemberSignature
) : Context(cadetClass) {

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

    /** Returns the type associated with the given [name] for this context.
     *  @return Parameter, local variable, or class field type, with the given [name].
     *  If neither of 3 is found, null is returned
     */
    fun getContextScopedVariableType(name: String): String? {
        cadetMember.params.find { param ->
            param.name == name
        }?.let { return it.type }

        cadetMember.localVariables.find { field ->
            field.name == name
        }?.let { return it.type }

        cadetClass.fields.find { field ->
            field.name == name
        }?.let { return it.type }

        return null
    }
}