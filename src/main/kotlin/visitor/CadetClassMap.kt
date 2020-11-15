package visitor

import model.CadetClass
import model.CadetField
import model.CadetMember
import signature.SignableCadetMember
import signature.MemberSignature

class CadetClassMap {
    private val classes = mutableListOf<CadetClass>()

    /**
     * Adds the given [CadetClass] to this class map.
     */
    fun addCadetClass(cadetClass: CadetClass) {
        classes.add(cadetClass)
    }

    /**
     * @return [CadetMember] reference with the matching [signature], from the [CadetClass] specified by [className].
     * Returns null if class or member are not found.
     */
    fun getCadetMember(className: String, signature: MemberSignature): CadetMember? {
        val cadetClass = classes.find { it.name == className }
        cadetClass?.let {
            return it.members.find { member ->
                signature.compareTo(SignableCadetMember(member))
            }
        }
        return null
    }

    /**
     * @return [CadetField] reference which belongs to the given [CadetClass].
     * Returns null if class or field are not found.
     */
    fun getCadetField(className: String, fieldName: String): CadetField? {
        val cadetClass = classes.find { it.name == className }
        cadetClass?.let {
            return it.fields.find { field ->
                field.name == fieldName
            }
        }
        return null
    }
}