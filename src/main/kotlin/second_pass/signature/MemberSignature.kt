package second_pass.signature

import second_pass.hierarchy.HierarchyGraph
import second_pass.resolver.ResolverProxy
import second_pass.resolver.resolver_tree.service.Resolver

/**
 * Object representing a unique second_pass.signature for a [SignableMember].
 * Uniqueness is guaranteed within the defining class scope.
 */
class MemberSignature(signable: SignableMember) {

    private val name: String = signable.name()
    private val nameHash: Int = signable.name().hashCode()
    private val numberOfParameters: Int = signable.getNumberOfParameters()
    private val paramTypes = mutableListOf<String>()

    private lateinit var hierarchyGraph: HierarchyGraph

    init {
        signable.getParameterTypes().forEach { paramTypes.add(it) }
    }

    fun withHierarchyGraph(hierarchyGraph: HierarchyGraph): MemberSignature {
        this.hierarchyGraph = hierarchyGraph
        return this
    }

    fun getMemberName() = name

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
                if (pThis == Resolver.WildcardType)
                    continue
                if (pOther == Resolver.WildcardType)
                    error("Parameter type cannot be Wildcard.")
                if (isSuperType(pOther, pThis)) continue
                if (isDependencyInjection(pThis, pOther)) continue

                return false
            }
            return true
        }
    }

    private fun isSuperType(parentType: String, childType: String): Boolean {
        return hierarchyGraph.isSuperType(childType, parentType)
    }

    private fun isDependencyInjection(className: String, interfaceName: String): Boolean {
        return hierarchyGraph.isImplementation(className, interfaceName)
    }

    override fun toString(): String {
        return "$name ($paramTypes)"
    }
}