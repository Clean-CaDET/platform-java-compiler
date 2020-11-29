package resolver

import model.CadetMember
import signature.MemberSignature

interface SymbolMap {

    fun findCadetMemberInContext(className: String?, signature: MemberSignature): CadetMember?
    fun getContextClassName(): String
    fun getContextScopedType(name: String): String
}
