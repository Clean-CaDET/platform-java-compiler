package resolver

import model.CadetClass
import model.CadetMember
import model.abs.CadetVariable
import signature.MemberSignature

interface SymbolContextMap {

    fun getClassAt(index: Int): CadetClass
    fun getClasses(): List<CadetClass>
    fun addClass(cadetClass: CadetClass)

    fun getVariableInContext(name: String): CadetVariable?
    fun getMember(callerName: String?, signature: MemberSignature): CadetMember?
    fun getField(callerType: String, variableName: String): CadetVariable?

    fun getContextClassName(): String
    fun getContextClassSuperType(): String?
    fun modifyCurrentClassParent(superClassName: String)

    fun <T> notifyUsage(resolvedReference: T)
}
