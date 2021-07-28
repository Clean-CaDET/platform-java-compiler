package second_pass.resolver.resolver_tree.service

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.expr.*
import second_pass.resolver.resolver_tree.model.NodeType
import second_pass.resolver.resolver_tree.model.ReferenceNode
import second_pass.resolver.resolver_tree.model.SimpleNode
import util.AstNodeUtil

class Builder {

    fun build(node: Node): ReferenceNode? {
        val root = buildRootNode(node) ?: return null
        recursiveChildNodeAddition(root)
        return root
    }

    private fun buildRootNode(node: Node): ReferenceNode? {
        val type = mapReferenceNodeType(node) ?: return null
        return ReferenceNode(node, type)
    }

    private fun recursiveChildNodeAddition(node: SimpleNode) {
        if (node !is ReferenceNode)
            return
        AstNodeUtil.iterateChildrenWithFunction(
            node.astNode,
            function = { astChildNode ->
                buildAnyNode(astChildNode)?.let { childResolverNode ->
                    node.children.add(childResolverNode)
                }
            }
        )
        node.children.forEach { recursiveChildNodeAddition(it) }
    }

    private fun buildAnyNode(node: Node): SimpleNode? {
        mapSimpleNodeType(node)?.let { type ->
            return SimpleNode(node, type)
        }
        mapReferenceNodeType(node)?.let { type ->
            return ReferenceNode(node, type)
        }
        return null
    }

    private fun mapSimpleNodeType(node: Node): NodeType? {
        return when(node) {
            is LiteralExpr -> NodeType.Literal
            is CastExpr -> NodeType.Cast
            is NullLiteralExpr -> NodeType.Null
            is ThisExpr -> NodeType.This
            is SuperExpr -> NodeType.Super
            is EnclosedExpr -> NodeType.Enclosed
            else -> return null
        }
    }

    private fun mapReferenceNodeType(node: Node): NodeType? {
        return when(node) {
            is MethodCallExpr -> NodeType.Method
            is ObjectCreationExpr -> NodeType.Constructor
            is NameExpr -> NodeType.Name
            is FieldAccessExpr -> NodeType.Field
            else -> null
        }
    }
}