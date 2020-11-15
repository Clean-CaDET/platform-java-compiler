package model

class CadetMember() {
    lateinit var name: String
    lateinit var cadetMemberType: CadetMemberType
    lateinit var source: String
    lateinit var parent: CadetClass
    lateinit var returnType: String
    // TODO Possibly add return type for symbol solving?

    val invokedMethods = mutableSetOf<CadetMember>()
    val accessedAccessors = mutableSetOf<CadetMember>()
    val accessedFields = mutableSetOf<CadetField>()
    val params = mutableListOf<CadetParameter>()
    val localVariables = mutableListOf<CadetLocalVariable>()

    // TODO Implement modifiers for Java
}