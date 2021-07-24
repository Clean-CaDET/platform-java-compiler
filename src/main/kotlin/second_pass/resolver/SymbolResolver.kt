package second_pass.resolver

import com.github.javaparser.ast.Node
import second_pass.resolver.resolver_tree.CadetReferenceUsageProxy
import second_pass.resolver.resolver_tree.ResolverTree
import util.Console

class SymbolResolver {

    companion object {
        const val WildcardType: String = "#"
    }

    private val resolverTreeBuilder = ResolverTree.Builder()
    private val resolverTreeResolver = ResolverTree.Resolver()
    private val resolverTreeUsageRecorder = ResolverTree.UsageRecorder()

    fun resolve(node: Node, scopeContext: ScopeContext) {
        // Build resolver tree
        val resolverTreeRoot = resolverTreeBuilder.build(node)

        // Resolve references
        resolverTreeResolver.resolve(resolverTreeRoot, scopeContext)

        // Record reference usages
        resolverTreeUsageRecorder.recordReferenceUsages(
            resolverTreeRoot,
            CadetReferenceUsageProxy(scopeContext.getCurrentCadetMember())
        )
    }
}