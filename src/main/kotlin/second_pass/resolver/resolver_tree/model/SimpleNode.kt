package second_pass.resolver.resolver_tree.model

import com.github.javaparser.ast.Node

open class SimpleNode(val astNode: Node, val type: NodeType) {
    lateinit var returnType: String
}