package first_pass.prototype_dto

class InterfacePrototype(val interfaceName: String) : JavaPrototype {

    override fun getName(): String {
        return interfaceName
    }
}