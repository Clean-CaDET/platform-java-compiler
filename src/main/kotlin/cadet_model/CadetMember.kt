package cadet_model

import second_pass.signature.SignableMember

class CadetMember: SignableMember {
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
}