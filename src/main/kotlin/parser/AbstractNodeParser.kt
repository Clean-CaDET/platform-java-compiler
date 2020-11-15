package parser

import com.github.javaparser.ast.Node

internal abstract class AbstractNodeParser {

    /**
     * @return First [T] type child within [Node.childNodes], or null if none are found.
     */
    protected inline fun <reified T : Node> getChildByType(node: Node): T? {
        return node.childNodes.find { it is T } as T?
    }

    /**
     * @return All [T] type children for the given [Node].
     */
    protected inline fun <reified T : Node> getChildrenByType(node: Node): List<T> {
        return node.childNodes.filterIsInstance<T>()
    }

    /**
     * @return Index of the first child node which is of type [T].
     * Null if no children are of type [T].
     */
    protected inline fun <reified T : Node> getChildIndex(node: Node): Int? {
        node.childNodes.indexOfFirst { childNode ->
            childNode is T
        }.also { index ->
            if (index == -1) return null
            return index
        }
    }
}