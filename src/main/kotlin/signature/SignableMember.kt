package signature

import model.CadetParameter

interface SignableMember {
    fun getName(): String
    fun getParameterTypes(): List<String>
    fun getNumberOfParameters(): Int
}