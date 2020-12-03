package context

import model.CadetClass
import model.CadetField
import model.CadetMember
import signature.MemberSignature

open class ClassContext(val cadetClass: CadetClass) {

    fun getField(name: String): CadetField? {
        return cadetClass.fields.find { it.name == name }
    }
}