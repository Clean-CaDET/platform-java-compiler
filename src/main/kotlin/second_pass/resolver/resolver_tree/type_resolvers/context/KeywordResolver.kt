package second_pass.resolver.resolver_tree.type_resolvers.context

import second_pass.resolver.ScopeContext
import second_pass.resolver.SymbolResolver

object KeywordResolver {

    enum class Keyword {
        This, Super
    }

    fun resolve(keyword: Keyword, scopeContext: ScopeContext): String {
        return when(keyword) {
            Keyword.This -> scopeContext.getCurrentClassName()
            Keyword.Super -> scopeContext.getCurrentClassSuperType() ?: SymbolResolver.WildcardType
        }
    }
}