package second_pass.resolver.resolver_tree

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.expr.*
import util.AstNodeUtil

object ResolverTree {

    enum class NodeType {
        None,
        Method, Constructor,
        Name, Field,
        Literal, Cast, Null, Enclosed,
        This, Super,
    }

    open class SimpleNode(val astNode: Node, val type: NodeType) {
        lateinit var returnType: String
    }

    class ReferenceNode(node: Node, type: NodeType): SimpleNode(node, type) {
        val children = mutableListOf<SimpleNode>()
    }

    class Builder {

        fun build(node: Node): ReferenceNode {
            val root = buildRootNode(node)
            recursiveChildNodeAddition(root)
            return root
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
                println("Building simple node of type $type")
                return SimpleNode(node, type)
            }
            mapReferenceNodeType(node)?.let { type ->
                println("Building reference node of type $type")
                return ReferenceNode(node, type)
            }
            return null
        }

        private fun buildRootNode(node: Node): ReferenceNode {
            mapReferenceNodeType(node)?.let { type ->
                println("Building root node of type $type")
                return ReferenceNode(node, type)
            }
            error("Invalid AST node [${node.metaModel.typeName}] conversion to ResolverTree.ReferenceNode.")
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
}