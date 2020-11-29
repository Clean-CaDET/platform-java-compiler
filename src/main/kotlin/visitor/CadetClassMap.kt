package visitor

import context.Context
import context.MemberContext
import javassist.NotFoundException
import model.CadetClass
import model.CadetField
import model.CadetMember
import resolver.SymbolMap
import signature.SignableCadetMember
import signature.MemberSignature

class CadetClassMap : SymbolMap {
    val classes = mutableListOf<CadetClass>()
    private lateinit var classContext: Context
    private lateinit var memberContext: MemberContext

    fun createClassContext(cadetClass: CadetClass) { classContext = Context(cadetClass) }
    fun createMemberContext(signature: MemberSignature) { memberContext = MemberContext(classContext, signature) }
    fun getMemberContext() = memberContext

    private fun currentClass() = classContext.cadetClass

    // SymbolMap overriden methods
    override fun getCurrentClassName(): String = currentClass().name
    override fun getCadetMember(className: String?, signature: MemberSignature): CadetMember? {
        getCallerClass(className)
        .apply {
            return this.members.find { member ->
                signature.compareTo(SignableCadetMember(member))
            }
        }
    }

    private fun getCallerClass(caller: String?): CadetClass {
        caller ?: return currentClass()

        classContext.getContextScopedCadetField(caller)
            ?.let {field ->
                findCadetClass(field.type) ?.let { return it }
            }

        memberContext.getContextScopedLocalVariable(caller)
            ?.let {variable ->
                findCadetClass(variable.type) ?.let { return it }
            }

        memberContext.getContextScopedParameter(caller)
            ?.let {param ->
                findCadetClass(param.type) ?.let { return it }
            }

        classes.find { c -> c.name == caller }
            ?.let { return it}


        throw NotFoundException("Caller: '${caller}' is unidentified.")
    }


    private fun findCadetClass(className: String): CadetClass? {
        return classes.find { c -> c.name == className }
    }
}