package second_pass.resolver.resolver_tree.model

import com.github.javaparser.ast.Node

class ReferenceNode(node: Node, type: NodeType): SimpleNode(node, type) {
    val children = mutableListOf<SimpleNode>()
    lateinit var resolvedReference: Any

    fun isResolved(): Boolean = this::resolvedReference.isInitialized
}