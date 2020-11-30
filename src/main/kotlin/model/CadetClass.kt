package model

import signature.CadetMemberSignature
import signature.MemberSignature

class CadetClass {
    var parent: CadetClass? = null
    lateinit var name: String
    lateinit var fullName: String
    val members = mutableListOf<CadetMember>()
    val fields = mutableListOf<CadetField>()

    fun getMemberViaSignature(signature: MemberSignature): CadetMember?
        = members.find { member -> signature.compareTo(CadetMemberSignature(member)) }

    fun getField(fieldName: String): CadetField?
            = fields.find { field -> field.name == fieldName}
}