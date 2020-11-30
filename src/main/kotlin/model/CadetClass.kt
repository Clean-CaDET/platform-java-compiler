package model

import signature.CadetMemberSignature
import signature.MemberSignature

class CadetClass {
    var parent: CadetClass? = null
    lateinit var name: String
    lateinit var fullName: String
    val members = mutableListOf<CadetMember>()
    val fields = mutableListOf<CadetField>()

    fun findMemberViaSignature(signature: MemberSignature): CadetMember? {
        return members.find { member -> signature.compareTo(CadetMemberSignature(member)) }
    }
}