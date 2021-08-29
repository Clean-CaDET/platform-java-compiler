package second_pass.resolver.resolver_tree.service

import cadet_model.CadetStaticClassName
import com.github.javaparser.ast.expr.CastExpr
import com.github.javaparser.ast.expr.LiteralExpr
import second_pass.resolver.InjectedContext
import second_pass.resolver.resolver_tree.model.NodeType
import second_pass.resolver.resolver_tree.model.ReferenceNode
import second_pass.resolver.resolver_tree.model.SimpleNode
import second_pass.resolver.resolver_tree.static_resolvers.context.KeywordResolver
import second_pass.resolver.resolver_tree.static_resolvers.reference.ConstructorCallResolver
import second_pass.resolver.resolver_tree.static_resolvers.reference.FieldAccessResolver
import second_pass.resolver.resolver_tree.static_resolvers.reference.MethodCallResolver
import second_pass.resolver.resolver_tree.static_resolvers.reference.NameAccessResolver
import second_pass.resolver.resolver_tree.static_resolvers.simple.CastResolver
import second_pass.resolver.resolver_tree.static_resolvers.simple.LiteralResolver

class Resolver {

    companion object {
        const val WildcardType: String = "#"
    }

    // Note that we could place InjectedContext to be a field, but multi-threading won't be doable then
    fun resolve(resolverTreeRoot: ReferenceNode, injectedContext: InjectedContext) {
        postOrderTraversalWithFunction(resolverTreeRoot, injectedContext, this::resolveInternal)
    }

    private fun postOrderTraversalWithFunction(
        node: SimpleNode,
        injectedContext: InjectedContext,
        function: (node: SimpleNode, injectedContext: InjectedContext) -> Unit
    ) {
        if (node is ReferenceNode) {
            node.children.forEach {
                child -> postOrderTraversalWithFunction(child, injectedContext, function)
            }
        }
        function(node, injectedContext)
    }

    private fun resolveInternal(node: SimpleNode, injectedContext: InjectedContext) {
        node.returnType =
            when(node.type) {
                // Simple
                NodeType.Literal -> LiteralResolver.resolve(node.astNode as LiteralExpr)
                NodeType.Cast -> CastResolver.resolve(node.astNode as CastExpr)
                NodeType.Null -> WildcardType
                NodeType.Enclosed -> error("Enclosed node found")

                // Context dependent
                NodeType.This -> KeywordResolver.resolve(KeywordResolver.Keyword.This, injectedContext)
                NodeType.Super -> KeywordResolver.resolve(KeywordResolver.Keyword.Super, injectedContext)

                // Reference query
                NodeType.Method -> {
                    internalResolve(
                        resolveFunction = MethodCallResolver::resolve,
                        node = node as ReferenceNode,
                        injectedContext = injectedContext,
                        success = {
                            node.resolvedReference = it
                            it.returnType
                        },
                        error = {
                            WildcardType
                        }
                    )
                }
                NodeType.Constructor -> {   // TODO Calling empty constructors while assuming their default presence won't work because SigQuery cannot find it (not explicitly defined)
                    internalResolve(
                        resolveFunction = ConstructorCallResolver::resolve,
                        node = node as ReferenceNode,
                        injectedContext = injectedContext,
                        success = {
                            node.resolvedReference = it
                            it.returnType
                        },
                        error = {
                            WildcardType
                        }
                    )
                }
                NodeType.Name -> {
                    internalResolve(
                        resolveFunction = NameAccessResolver::resolve,
                        node = node as ReferenceNode,
                        injectedContext = injectedContext,
                        success = {
                            node.resolvedReference = it
                            it.type
                        },
                        error = {
                            WildcardType
                        }
                    )
                }
                NodeType.Field -> {
                    internalResolve(
                        resolveFunction = FieldAccessResolver::resolve,
                        node = node as ReferenceNode,
                        injectedContext = injectedContext,
                        success = {
                            if (it !is CadetStaticClassName)
                                node.resolvedReference = it
                            it.type
                        },
                        error = {
                            WildcardType
                        }
                    )
                }
            }
    }

    private fun <T> internalResolve(
        resolveFunction: (node: ReferenceNode, injectedContext: InjectedContext) -> T?,
        node: ReferenceNode, injectedContext: InjectedContext,
        success: (ref: T) -> String,
        error: () -> String
    ): String {
        val ref = resolveFunction(node, injectedContext)
        return if (ref != null) success(ref)
        else error()
    }
}