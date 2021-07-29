package second_pass.resolver

import com.github.javaparser.ast.Node
import kotlinx.coroutines.runBlocking
import second_pass.resolver.resolver_tree.service.Builder
import second_pass.resolver.resolver_tree.service.Resolver
import second_pass.resolver.resolver_tree.service.UsageRecorder

class ResolverProxy {

    private val builder = Builder()
    private val resolver = Resolver()
    private val usageRecorder = UsageRecorder()

    fun resolve(node: Node, injectedContext: InjectedContext) {
        // Build resolver tree
        val resolverTreeRoot = builder.build(node) ?: return

        // Resolve references
        resolver.resolve(resolverTreeRoot, injectedContext)

        // Record reference usages
        // TODO Add threading support here through an MQ or something
        usageRecorder.recordReferenceUsages(
            resolverTreeRoot,
            injectedContext.getCurrentCadetMember()
        )
    }
}