package second_pass.resolver

import com.github.javaparser.ast.Node
import second_pass.resolver.resolver_tree.ResolverTree
import util.Console

class SymbolResolver {

    companion object {
        const val WildcardType: String = "#"
    }

    private lateinit var wizard: ResolverWizard
    fun getWizard() = wizard;

    fun resolve(node: Node, wizard: ResolverWizard) {
        val sNode = createSolverNode(node)
//        sNode ?: error("Unresolvable node type: ${node.metaModel.typeName}.")
//        this.wizard = wizard;
//        sNode.resolve()
    }

    fun createSolverNode(node: Node)/*: BaseSolverNode?*/ {
        val builder = ResolverTree.Builder()
        val root = builder.build(node)
        Console.printResolverTree(root)
        println("____________________________________________")
//        return when (node) {
//            is MethodCallExpr -> MethodSolverNode(node, this)
//            is ObjectCreationExpr -> ConstructorSolverNode(node, this)
//
//            is LiteralExpr -> LiteralSolverNode(node)
//            is CastExpr -> CastSolverNode(node, this)
//            is NullLiteralExpr -> NullSolverNode(node)
//
//            is ThisExpr -> ThisSolverNode(node, this)
//            is SuperExpr -> SuperSolverNode(node, this)
//
//            is NameExpr -> NameSolverNode(node, this)
//            is FieldAccessExpr -> FieldAccessSolverNode(node, this)
//
//            is EnclosedExpr -> EnclosedExprSolverNode(node, this)
//            else -> return null
//        }
    }
}