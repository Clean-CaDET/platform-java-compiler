package second_pass.resolver.resolver_tree.static_resolvers.context

import second_pass.resolver.InjectedContext
import second_pass.resolver.SymbolResolver

object KeywordResolver {

    enum class Keyword {
        This, Super
    }

    fun resolve(keyword: Keyword, injectedContext: InjectedContext): String {
        return when(keyword) {
            Keyword.This -> injectedContext.getCurrentClassName()
            Keyword.Super -> injectedContext.getCurrentClassSuperType() ?: SymbolResolver.WildcardType
        }
    }
}