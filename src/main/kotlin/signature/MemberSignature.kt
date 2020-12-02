package signature

import resolver.SymbolContextMap
import resolver.SymbolResolver
import java.lang.IllegalArgumentException

/**
 * Object representing a unique signature for a [SignableMember].
 * Uniqueness is guaranteed within the defining class scope.
 */
class MemberSignature(signable: SignableMember) {
    private var nameHash = 0
    private var numberOfParameters = 0
    private val paramTypes = mutableListOf<String>()

    companion object {
        private lateinit var symbolMap: SymbolContextMap

        fun loadSymbolMap(symbolMap: SymbolContextMap) {
            MemberSignature.symbolMap = symbolMap
        }
    }

    init {
        generateHash(signable)
    }

    private fun generateHash(signable: SignableMember) {
        nameHash = signable.getName().hashCode()
        numberOfParameters = signable.getNumberOfParameters()
        signable.getParameterTypes().forEach { paramTypes.add(it) }
    }

    /**
     * Compares the signature of the given [signable] to this signature.
     * @return True if the signature of the given [signable] is the same as this signature.
     * False if not.
     */
    fun compareTo(signable: SignableMember): Boolean {
        MemberSignature(signable).also {
            if (this.nameHash != it.nameHash) return false
            if (this.numberOfParameters != it.numberOfParameters) return false

            for (i in paramTypes.indices) {
                val pThis = this.paramTypes[i]
                val pOther = it.paramTypes[i]

                if (pThis == SymbolResolver.WildcardType)
                    continue
                if (pOther == SymbolResolver.WildcardType)
                    throw IllegalArgumentException("Argument param type cannot be Wildcard.")
                if (pThis != pOther) {
                    if (isSuperType(pOther, pThis)) continue
                    if (isDependencyInjection(pThis, pOther)) continue
                    return false
                }
            }
            return true
        }
    }

    private fun isSuperType(parentType: String, childType: String): Boolean {
        return symbolMap.isSuperType(childType, parentType)
    }

    private fun isDependencyInjection(className: String, interfaceName: String): Boolean {
        return symbolMap.containsInterface(className, interfaceName)
    }
}