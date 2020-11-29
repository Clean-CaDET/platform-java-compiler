package resolver

import model.CadetMember
import signature.MemberSignature

interface SymbolMap {

    fun getCadetMember(className: String?, signature: MemberSignature): CadetMember?
    fun getCurrentClassName(): String
}
