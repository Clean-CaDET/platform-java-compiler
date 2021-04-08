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

    /**
     * @return Index of the first child node which is of type [T].
     * Null if no children are of type [T].
     */
    inline fun <reified T : Node> getChildIndex(node: Node): Int? {
        node.childNodes.indexOfFirst { childNode ->
            childNode is T
        }.also { index ->
            if (index == -1) return null
            return index
        }
    }

    /** Returns all direct child nodes from the given node argument in a list */
    fun getDirectChildNodes(node: Node): List<Node> {
        return mutableListOf<Node>().apply {
            node.childNodes.forEach {child ->
                this.add(child)
            }
        }
    }
}