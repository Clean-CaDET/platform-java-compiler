package signature

import resolver.SymbolContextMap

/**
 * Object representing a unique signature for a [SignableMember].
 * Uniqueness is guaranteed within the defining class scope.
 */
class MemberSignature(signable: SignableMember) {
    private var hash = 0

    companion object {
        private lateinit var symbolMap: SymbolContextMap

        fun addSymbolMap(symbolMap: SymbolContextMap) {
            MemberSignature.symbolMap = symbolMap
        }
    }

    init {
        this.hash = generateHash(signable)
    }

    private fun generateHash(signable: SignableMember): Int {
        var result = signable.getName().hashCode()
        result = 31 * result + signable.getNumberOfParameters()
        signable.getParameterTypes().forEach { paramType ->
            result = 31 * result + paramType.hashCode()
        }
        return result
    }

    /**
     * Compares the signature of the given [signable] to this signature.
     * @return True if the signature of the given [signable] is the same as this signature.
     * False if not.
     */
    fun compareTo(signable: SignableMember): Boolean {
        return this.hash == generateHash(signable)
    }
}