package model

class CadetClass {
    var parent: CadetClass? = null
    lateinit var name: String
    lateinit var fullName: String
    val members = mutableListOf<CadetMember>()
    val fields = mutableListOf<CadetField>()
}