package cadet_model

import second_pass.resolver.resolver_tree.service.CadetReferenceUsageProxy
import second_pass.signature.SignableMember

class CadetMember: SignableMember, CadetReferenceUsageProxy {
    lateinit var name: String
    lateinit var cadetMemberType: CadetMemberType
    lateinit var source: String
    lateinit var parent: CadetClass
    lateinit var returnType: String
    val params = mutableListOf<CadetParameter>()

    // TODO This will be a List for testing purposes, return to Set for production
    val invokedMethods = mutableListOf<CadetMember>()
    val accessedAccessors = mutableListOf<CadetMember>()
    val accessedFields = mutableListOf<CadetField>()


    // TODO Implement modifiers for Java

    override fun name(): String = name
    override fun getParameterTypes(): List<String> = params.map { param -> param.type }
    override fun getNumberOfParameters(): Int = params.size

    override fun recordReferenceUsage(reference: Any) {
        when(reference) {
            is CadetMember -> {
                invokedMethods.add(reference)
                println("[Record] Member ${reference.name} invoked in member ${parent.name}.${name}")
            }
            is CadetField -> {
                accessedFields.add(reference)
                println("[Record] Field ${reference.name} accessed in member ${parent.name}.${name}")
            }
            is CadetParameter -> {
                println("[Record] Parameter ${reference.name} accessed in member ${parent.name}.${name}")
            }
            is CadetLocalVariable -> {
                println("[Record] Local variable ${reference.name} accessed in member ${parent.name}.${name}")
            }
            else -> error("Unsupported reference type being recorded: [${reference.javaClass}]")
        }
    }
}