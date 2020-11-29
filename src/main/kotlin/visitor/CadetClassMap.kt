package visitor

import javassist.NotFoundException
import model.CadetClass
import model.CadetField
import model.CadetMember
import resolver.SymbolMap
import signature.SignableCadetMember
import signature.MemberSignature

class CadetClassMap : SymbolMap {
    val classes = mutableListOf<CadetClass>()
    lateinit var currentClass: CadetClass

    override fun getCurrentClassName(): String = currentClass.name

    override fun getCadetMemberReturnType(className: String?, signature: MemberSignature): String? {
        getCadetMember(className, signature)
            ?.let { return it.returnType }
        return null
    }

    override fun getCadetMember(memberCaller: String?, signature: MemberSignature): CadetMember? {
        getCallerClass(memberCaller)
        .apply {
            return this.members.find { member ->
                signature.compareTo(SignableCadetMember(member))
            }
        }
    }

    private fun getCallerClass(caller: String?): CadetClass {
        caller ?: return currentClass
        classes.find { c -> c.name == caller }
            ?.let { return it}
        getCadetField(currentClass.name, caller)
            ?.let {field ->
                findCadetClass(field.type) ?.let { foundClass -> return foundClass }
            }
        throw NotFoundException("Caller: '${caller}' is unidentified.")
    }

    fun getCadetField(className: String?, fieldName: String): CadetField? {
        classes.find {
            if (className != null) it.name == className
            else it.name == currentClass.name
        }
        ?.let {
            return it.fields.find { field ->
                field.name == fieldName
            }
        }
        return null
    }

    private fun findCadetClass(className: String): CadetClass? {
        return classes.find { c -> c.name == className }
    }
}