package second_pass.resolver.resolver_tree.static_resolvers.context

import second_pass.resolver.InjectedContext
import second_pass.resolver.ResolverProxy
import second_pass.resolver.resolver_tree.service.Resolver

object KeywordResolver {

    enum class Keyword {
        This, Super
    }

    fun resolve(keyword: Keyword, injectedContext: InjectedContext): String {
        return when(keyword) {
            Keyword.This -> injectedContext.getCurrentClassName()
            Keyword.Super -> injectedContext.getCurrentClassSuperType() ?: Resolver.WildcardType
        }
    }
}