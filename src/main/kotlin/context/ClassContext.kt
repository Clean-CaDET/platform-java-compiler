package context

import model.CadetClass
import model.CadetField
import model.CadetMember
import signature.MemberSignature

open class ClassContext(val cadetClass: CadetClass) {

    fun getContextScopedField(name: String): CadetField? {
        return cadetClass.fields.find { field ->
            field.name == name
        }
    }

    fun getContextScopedMethod(signature: MemberSignature): CadetMember? {
        return cadetClass.getMemberViaSignature(signature)
    }
}