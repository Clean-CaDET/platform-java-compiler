package second_pass.signature

interface SignableMember {
    fun getName(): String
    fun getParameterTypes(): List<String>
    fun getNumberOfParameters(): Int
}