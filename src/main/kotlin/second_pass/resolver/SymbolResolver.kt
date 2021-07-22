package second_pass.resolver

import com.github.javaparser.ast.Node
import second_pass.resolver.resolver_tree.ResolverTree
import util.Console

class SymbolResolver {

    companion object {
        const val WildcardType: String = "#"
    }

    private val resolverTreeBuilder = ResolverTree.Builder()
    private val resolverTreeResolver = ResolverTree.Resolver()

    fun resolve(node: Node, wizard: ResolverWizard) {
        val resolverTreeRoot = buildResolverTree(node); Console.printResolverTree(resolverTreeRoot)
        resolverTreeResolver.resolve(resolverTreeRoot)
    }

    private fun buildResolverTree(node: Node): ResolverTree.ReferenceNode {
        return ResolverTree.Builder().build(node)
    }
}