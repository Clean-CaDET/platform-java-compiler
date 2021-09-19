package cadet_model

import second_pass.resolver.resolver_tree.service.CadetReferenceUsageProxy
import second_pass.signature.SignableMember

class CadetMember: SignableMember, CadetReferenceUsageProxy {
    lateinit var name: String
    lateinit var cadetMemberType: CadetMemberType
    lateinit var source: String
    lateinit var parent: CadetClass
    lateinit var returnType: String
    val params = mutableSetOf<CadetParameter>()
    val localVariables = mutableSetOf<CadetLocalVariable>()

    val invokedMethods = mutableSetOf<CadetMember>()
    val accessedAccessors = mutableSetOf<CadetMember>()
    val accessedFields = mutableSetOf<CadetField>()
    val modifiers = mutableSetOf<CadetModifier>()

    override fun name(): String = name
    override fun getParameterTypes(): List<String> = params.map { param -> param.type }
    override fun getNumberOfParameters(): Int = params.size

    override fun recordReferenceUsage(reference: Any) {
        when(reference) {
            is CadetMember -> invokedMethods.add(reference)
            is CadetField -> accessedFields.add(reference)
            is CadetParameter -> {}
            is CadetLocalVariable -> {}
            is CadetStaticClassName -> {}
            else -> error("Unsupported reference type being recorded: [${reference.javaClass}]")
        }
    }
}