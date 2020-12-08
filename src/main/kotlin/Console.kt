import cadet_model.*
import com.github.javaparser.ast.Node

// Test class only
object Console {

    /** Recursively prints the given [Node] and its children in a stair-like sequence
     *  until all descendants are displayed. */
    fun printTree(node: Node) {
        recursiveTreePrint(node, 0)
    }

    private fun recursiveTreePrint(node: Node, tabs: Int) {
        var counter = 0
        while (counter < tabs) {
            print("\t")
            counter++
        }
        println(node.metaModel.typeName)
        node.childNodes.forEach { childNode ->
            recursiveTreePrint(childNode, tabs + 1)
        }
    }

    fun printCadetClass(cadetClass: CadetClass) {
        val builder = StringBuilder()
        builder.append("\nClass ${cadetClass.name} \nFull name: ${cadetClass.fullName}\n")

        // Methods
        builder.append("Methods: ").append("\n")
        cadetClass.members
            .filter { it.cadetMemberType == CadetMemberType.Method }
            .forEach { method -> printMember(method, builder) }

        // Constructors
        builder.append("Constructors:").append("\n")
        cadetClass.members
            .filter { it.cadetMemberType == CadetMemberType.Constructor }
            .forEach { constructor -> printMember(constructor, builder) }

        // Fields
        builder.append("Fields:").append("\n")
        cadetClass.fields
            .forEach { field -> printField(field, builder) }

        println(builder.toString())
    }

    private fun printMember(it: CadetMember, builder: java.lang.StringBuilder) {
        builder.append("\t ${it.returnType} ${it.name}")
        printParams(it.params, builder)
        if (it.invokedMethods.isNotEmpty()) {
            builder.append("\t\tInvoked members:\n")
            it.invokedMethods.forEach { invokedMethod ->
                builder.append("\t\t\t${invokedMethod.name}() : ${invokedMethod.parent.name}\n")
            }
        }
        if (it.accessedFields.isNotEmpty()) {
            builder.append("\t\tAccessed fields:\n")
            it.accessedFields.forEach { field ->
                builder.append("\t\t\t${field.type} ${field.name}\n")
            }
        }
        if (it.localVariables.isNotEmpty()) {
            builder.append("\t\tLocal variables:\n")
            it.localVariables.forEach { localVariable ->
                builder.append("\t\t\t${localVariable.type} ${localVariable.name}\n")
            }
        }
    }

    private fun printField(field: CadetField, builder: java.lang.StringBuilder) {
        builder.append("\t")
        field.modifiers.forEach { modifier ->
            builder.append(modifier.type).append(" ")
        }
        builder.append("${field.type} ")
        builder.append(field.name).append("\t")
        builder.append("\n")
    }

    private fun printParams(paramList: List<CadetParameter>, builder: java.lang.StringBuilder) {
        var flag = false
        builder.append("(")
        paramList.forEach { param ->
            builder.append("${param.type} ${param.name}, ")
            flag = true
        }
        if (flag)
            builder.delete(builder.length - 2, builder.length)
        builder.append(")\n")
    }
}