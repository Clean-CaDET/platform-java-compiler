package context

import javassist.NotFoundException
import model.CadetClass
import signature.MemberSignature

class ContextHolder {
    lateinit var classContext: ClassContext
    lateinit var memberContext: MemberContext

    fun createClassContext(cadetClass: CadetClass) {
        classContext = ClassContext(cadetClass)
    }

    fun createMemberContext(signature: MemberSignature) {
        memberContext = MemberContext(classContext, signature)
    }

    fun getContextScopedCadetClass(caller: String?, classes: List<CadetClass>): CadetClass {
        caller ?: return currentClass()

        memberContext.getContextScopedLocalVariable(caller)
            ?.let {variable ->
                findCadetClass(variable.type, classes) ?.let { return it }
            }

        memberContext.getContextScopedParameter(caller)
            ?.let {param ->
                findCadetClass(param.type, classes) ?.let { return it }
            }

        classContext.getContextScopedCadetField(caller)
            ?.let {field ->
                findCadetClass(field.type, classes) ?.let { return it }
            }

        classes.find { c -> c.name == caller }
            ?.let { return it; }

        throw NotFoundException("Caller: '${caller}' is unidentified.")
    }

    private fun findCadetClass(className: String, classes: List<CadetClass>) = classes.find { c -> c.name == className }
    private fun currentClass() = classContext.cadetClass
}