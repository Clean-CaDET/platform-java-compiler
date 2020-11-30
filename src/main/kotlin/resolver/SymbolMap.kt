package resolver

import model.CadetClass
import model.CadetMember
import signature.MemberSignature

interface SymbolMap {

    fun getClassAt(index: Int): CadetClass
    fun getClasses(): List<CadetClass>
    fun addClass(cadetClass: CadetClass)

    fun findCadetMemberInContext(name: String?, signature: MemberSignature): CadetMember?
    fun getContextClassName(): String
    fun getContextScopedType(name: String): String
    fun getContextSuperType(): String?

    fun modifyCurrentClassParent(superClassName: String)
}
