package signature

import model.CadetMember

class CadetMemberSignature(cadetMember: CadetMember) : SignableMember {

    private val name: String = cadetMember.name
    private val parameterTypes = mutableListOf<String>()

    init {
        cadetMember.params.forEach { parameterTypes.add(it.type) }
    }

    override fun getName(): String = name
    override fun getParameterTypes(): List<String> = parameterTypes
    override fun getNumberOfParameters(): Int = parameterTypes.size

}