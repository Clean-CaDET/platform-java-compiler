package second_pass.resolver

import com.github.javaparser.ast.Node
import second_pass.resolver.resolver_tree.service.Builder
import second_pass.resolver.resolver_tree.service.Resolver
import second_pass.resolver.resolver_tree.service.UsageRecorder

class SymbolResolver {

    companion object {
        const val WildcardType: String = "#"
    }

    private val builder = Builder()
    private val resolver = Resolver()
    private val usageRecorder = UsageRecorder()

    fun resolve(node: Node, injectedContext: InjectedContext) {
        // Build resolver tree
        val resolverTreeRoot = builder.build(node)

        // Resolve references
        resolver.resolve(resolverTreeRoot, injectedContext)

        // Record reference usages
        usageRecorder.recordReferenceUsages(
            resolverTreeRoot,
            injectedContext.getCurrentCadetMember()
        )
    }
}