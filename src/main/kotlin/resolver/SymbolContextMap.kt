package resolver

import model.CadetClass
import model.CadetMember
import model.interfaces.CadetVariable
import signature.MemberSignature

interface SymbolContextMap {

    fun getClassAt(index: Int): CadetClass
    fun getClasses(): List<CadetClass>
    fun addClass(cadetClass: CadetClass)

    fun getCadetVariableInContext(name: String): CadetVariable?
    fun getCadetMemberInContext(callerName: String?, signature: MemberSignature): CadetMember?
    fun getContextClassName(): String
    fun getContextClassSuperType(): String?

    fun modifyCurrentClassParent(superClassName: String)
    fun <T> notifyUsage(resolvedReference: T)
}
