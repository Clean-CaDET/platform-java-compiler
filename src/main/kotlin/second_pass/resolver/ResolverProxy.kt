package second_pass.resolver

import com.github.javaparser.ast.Node
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import second_pass.resolver.resolver_tree.service.Builder
import second_pass.resolver.resolver_tree.service.Resolver
import second_pass.resolver.resolver_tree.service.UsageRecorder

class ResolverProxy {

    private val builder = Builder()
    private val resolver = Resolver()
    private val usageRecorder = UsageRecorder()

    fun resolve(node: Node, injectedContext: InjectedContext) {
        val resolverTreeRoot = builder.build(node) ?: return

        resolver.resolve(resolverTreeRoot, injectedContext)

        usageRecorder.recordReferenceUsages(
            resolverTreeRoot,
            injectedContext.getCurrentCadetMember()
        )
    }

}