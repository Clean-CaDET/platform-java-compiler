package second_pass.signature

interface SignableMember {
    fun name(): String
    fun getParameterTypes(): List<String>
    fun getNumberOfParameters(): Int
}