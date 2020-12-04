package cadet_model

class CadetMember() {
    lateinit var name: String
    lateinit var cadetMemberType: CadetMemberType
    lateinit var source: String
    lateinit var parent: CadetClass
    lateinit var returnType: String

    // TODO This will be a List for testing purposes, return to Set for production
    val invokedMethods = mutableListOf<CadetMember>()
    val accessedAccessors = mutableListOf<CadetMember>()
    val accessedFields = mutableListOf<CadetField>()
    val params = mutableListOf<CadetParameter>()
    val localVariables = mutableListOf<CadetLocalVariable>()

    // TODO Implement modifiers for Java
}