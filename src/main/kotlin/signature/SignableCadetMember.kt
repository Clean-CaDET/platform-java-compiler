package signature

import model.CadetMember

class SignableCadetMember(private val cadetMember: CadetMember) : SignableMember {

    private val parameterTypes = mutableListOf<String>()

    init {
        cadetMember.params.forEach { param ->
            parameterTypes.add(param.type)
        }
    }

    override fun getName(): String = cadetMember.name
    override fun getParameterTypes(): List<String> = parameterTypes
    override fun getNumberOfParameters(): Int = parameterTypes.size

}