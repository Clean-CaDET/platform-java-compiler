package visitor

import model.CadetClass
import model.CadetField
import model.CadetMember
import signature.SignableCadetMember
import signature.MemberSignature
import java.lang.IllegalArgumentException

class CadetClassMap {
    private val classes = mutableListOf<CadetClass>()

    fun addCadetClass(cadetClass: CadetClass) {
        classes.add(cadetClass)
    }

    fun getAt(index: Int): CadetClass {
        if (index >= classes.size) throw IllegalArgumentException("Classmap index out of bounds. $index >= ${classes.size}")
        return classes[index]
    }

    fun getAllClasses() = classes

    /** Get all classes contained within this class map */
    fun getCadetClasses() = classes

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