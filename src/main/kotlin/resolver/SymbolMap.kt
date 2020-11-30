package resolver

import model.CadetMember
import signature.MemberSignature

interface SymbolMap {

    fun findCadetMemberInContext(name: String?, signature: MemberSignature): CadetMember?
    fun getContextClassName(): String
    fun getContextScopedType(name: String): String
    fun getContextSuperType(): String?
}
