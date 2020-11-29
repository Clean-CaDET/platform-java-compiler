package context

import model.CadetClass
import model.CadetField
import model.CadetMember
import signature.SignableCadetMember
import signature.MemberSignature

open class Context(val cadetClass: CadetClass) {

    fun getContextScopedCadetField(name: String): CadetField? {
        return cadetClass.fields.find { field ->
            field.name == name
        }
    }

    fun getContextScopedMethod(signature: MemberSignature): CadetMember? {
        return cadetClass.members.find {
            signature.compareTo(SignableCadetMember(it))
        }
    }
}