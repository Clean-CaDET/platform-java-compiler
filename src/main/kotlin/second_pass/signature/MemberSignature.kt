package second_pass.signature

import second_pass.hierarchy.HierarchyGraph
import second_pass.resolver.SymbolResolver
import java.lang.IllegalArgumentException

/**
 * Object representing a unique second_pass.signature for a [SignableMember].
 * Uniqueness is guaranteed within the defining class scope.
 */
class MemberSignature(
    signable: SignableMember,
    private val hierarchyGraph: HierarchyGraph? = null
) {
    private var nameHash = 0
    private var numberOfParameters = 0
    private val paramTypes = mutableListOf<String>()

    init {
        generateSignature(signable)
    }

    private fun generateSignature(signable: SignableMember) {
        nameHash = signable.getName().hashCode()
        numberOfParameters = signable.getNumberOfParameters()
        signable.getParameterTypes().forEach { paramTypes.add(it) }
    }

    /**
     * Compares the second_pass.signature of the given [signable] to this second_pass.signature.
     * @return True if the second_pass.signature of the given [signable] is the same as this second_pass.signature.
     * False if not.
     */
    fun compareTo(signable: SignableMember): Boolean {
        MemberSignature(signable).also {
            if (this.nameHash != it.nameHash) return false
            if (this.numberOfParameters != it.numberOfParameters) return false

            for (i in paramTypes.indices) {
                val pThis = this.paramTypes[i]
                val pOther = it.paramTypes[i]

                if (pThis == pOther) continue
                if (pThis == SymbolResolver.WildcardType)
                    continue
                if (pOther == SymbolResolver.WildcardType)
                    throw IllegalArgumentException("Parameter type cannot be Wildcard.")
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
        hierarchyGraph ?: throw NullPointerException("Hierarchy graph not injected into Member signature.")
        return hierarchyGraph.isSuperType(childType, parentType)
    }

    private fun isDependencyInjection(className: String, interfaceName: String): Boolean {
        hierarchyGraph ?: throw NullPointerException("Hierarchy graph not injected into Member signature.")
        return hierarchyGraph.isImplementation(className, interfaceName)
    }
}