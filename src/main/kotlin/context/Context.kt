package context

import model.CadetClass
import model.CadetField
import model.CadetMember
import signature.SignableCadetMember
import signature.MemberSignature

internal open class Context(protected val cadetClass: CadetClass) {

    fun getLocalField(fieldName: String): CadetField? {
        return cadetClass.fields.find { it.name == fieldName }
    }

    /**
     * @return [CadetMember] from this context, which has the matching [signature].
     * Null if no such members are found.
     */
    fun getLocalMethod(signature: MemberSignature): CadetMember? {
        return cadetClass.members.find {
            signature.compareTo(SignableCadetMember(it))
        }
    }
}