package prototype_dto

class InterfacePrototype(private val interfaceName: String) : JavaPrototype {

    override fun getName(): String {
        return interfaceName
    }
}