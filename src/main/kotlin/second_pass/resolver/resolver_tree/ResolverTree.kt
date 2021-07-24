package second_pass.resolver.resolver_tree

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.expr.*
import second_pass.resolver.ScopeContext
import second_pass.resolver.SymbolResolver
import second_pass.resolver.resolver_tree.type_resolvers.context.KeywordResolver
import second_pass.resolver.resolver_tree.type_resolvers.reference.MethodCallResolver
import second_pass.resolver.resolver_tree.type_resolvers.simple.CastResolver
import second_pass.resolver.resolver_tree.type_resolvers.simple.EnclosedResolver
import second_pass.resolver.resolver_tree.type_resolvers.simple.LiteralResolver
import util.AstNodeUtil

object ResolverTree {

    enum class NodeType {
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
        lateinit var resolvedReference: Any
    }

    class Builder {

        fun build(node: Node): ReferenceNode {
            val root = buildRootNode(node)
            recursiveChildNodeAddition(root)
            return root
        }

        private fun buildRootNode(node: Node): ReferenceNode {
            mapReferenceNodeType(node)?.let { type ->
//                println("Building root node of type $type")
                return ReferenceNode(node, type)
            }
            error("AST node of type [${node.metaModel.typeName}] cannot be a root node in a ResolverTree.")
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
//                println("Building simple node of type $type")
                return SimpleNode(node, type)
            }
            mapReferenceNodeType(node)?.let { type ->
//                println("Building reference node of type $type")
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

    class Resolver {

        // Note that we could place ScopeContext to be a field, but multi-threading won't be doable then
        fun resolve(resolverTreeRoot: SimpleNode, scopeContext: ScopeContext) {
            postOrderTraversalWithFunction(resolverTreeRoot, scopeContext, this::resolveInternal)
        }

        private fun postOrderTraversalWithFunction(
            node: SimpleNode,
            scopeContext: ScopeContext,
            function: (node: SimpleNode, scopeContext: ScopeContext) -> Unit
        ) {
            if (node is ReferenceNode) {
                node.children.forEach {
                        child -> postOrderTraversalWithFunction(child, scopeContext, function)
                }
            }
            function(node, scopeContext)
        }

        private fun resolveInternal(node: SimpleNode, scopeContext: ScopeContext) {
            node.returnType =
                when(node.type) {
                    // Simple
                    NodeType.Literal -> LiteralResolver.resolve(node.astNode as LiteralExpr)
                    NodeType.Cast -> CastResolver.resolve(node.astNode as CastExpr)
                    NodeType.Null -> SymbolResolver.WildcardType
                    NodeType.Enclosed -> EnclosedResolver.resolve(node.astNode as EnclosedExpr)

                    // Context dependent
                    NodeType.This -> KeywordResolver.resolve(KeywordResolver.Keyword.This, scopeContext)
                    NodeType.Super -> KeywordResolver.resolve(KeywordResolver.Keyword.Super, scopeContext)

                    // Reference query TODO
                    NodeType.Method -> {
                        MethodCallResolver.resolve(node as ReferenceNode, scopeContext)
                            ?.let {
                                node.resolvedReference = it
                                it.returnType
                            }
                        SymbolResolver.WildcardType
                    }
                    NodeType.Constructor -> ""
                    NodeType.Name -> ""
                    NodeType.Field -> ""
                }
        }


    }
}