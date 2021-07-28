package util

import com.github.javaparser.ast.Node

object AstNodeUtil {

    /**
     * @return First [T] type child within [Node.childNodes], or null if none are found.
     */
    inline fun <reified T : Node> getChildByType(node: Node): T? {
        return node.childNodes.find { it is T } as T?
    }

    /**
     * @return All [T] type children for the given [Node].
     */
    inline fun <reified T : Node> getChildrenByType(node: Node): List<T> {
        return node.childNodes.filterIsInstance<T>()
    }

    fun iterateChildrenWithFunction(node: Node, function: (child: Node) -> Unit) {
        node.childNodes.forEach(function)
    }
}