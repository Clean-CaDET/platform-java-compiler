package second_pass

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.body.*
import com.github.javaparser.ast.expr.FieldAccessExpr
import com.github.javaparser.ast.expr.MethodCallExpr
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import prototype_dto.ClassPrototype
import prototype_dto.InterfacePrototype
import prototype_dto.JavaPrototype
import second_pass.context.ClassContext
import second_pass.context.MemberContext
import second_pass.context.VisitorContext
import second_pass.resolver.SymbolResolver
import second_pass.resolver.node_parser.LocalVariableParser
import second_pass.signature.MemberDeclarationSignature
import second_pass.signature.MemberSignature
import util.AstNodeUtil

class SymbolResolverVisitor : VoidVisitorAdapter<ClassContext>() {

    private val visitorContext = VisitorContext()
    private lateinit var resolver: SymbolResolver

    // TODO Add internal context tracker which gets passed to Resolver during each iteration

    fun resolveSourceCode(resolverPairs: List<Pair<ClassOrInterfaceDeclaration, JavaPrototype>>)
            : List<JavaPrototype> {
        val prototypes = isolatePrototypes(resolverPairs)
        resolver = SymbolResolver(visitorContext, prototypes)
        resolvePrototypes(resolverPairs)
        return prototypes
    }

    private fun isolatePrototypes(resolverPairs: List<Pair<ClassOrInterfaceDeclaration, JavaPrototype>>)
            : List<JavaPrototype> {
        return resolverPairs.map { pair -> pair.second }
    }

    private fun resolvePrototypes(resolverPairs: List<Pair<ClassOrInterfaceDeclaration, JavaPrototype>>) {
        for (pair in resolverPairs) {
            if (pair.second is InterfacePrototype) continue
            // TODO Make this internal!
            visitorContext.createClassContext((pair.second as ClassPrototype).cadetClass)
            visitTopLevelChildren(pair.first)
        }
    }

    private fun visitTopLevelChildren(node: ClassOrInterfaceDeclaration) {
        AstNodeUtil
            .getDirectChildNodes(node)
            .forEach { child ->
                when (child) {
                    is MethodDeclaration -> visit(child, null)
                    is ConstructorDeclaration -> visit(child, null)
                    is FieldDeclaration -> visit(child, null)
                    else -> {}
                }
            }
    }

    override fun visit(node: ClassOrInterfaceDeclaration, arg: ClassContext?) { return }

    // TODO Fix this jumble with VisitorContext calls, it's ugly
    override fun visit(node: MethodDeclaration, arg: ClassContext?) {
        // TODO This should also be internal!
        if (visitorContext.hasMemberContext()) return   // This will stop the parser from entering anon classes and such because ATM it is not supported
        createMemberContext(node)
        super.visit(node, visitorContext.memberContext)
        visitorContext.removeMemberContext()
    }

    override fun visit(node: ConstructorDeclaration, arg: ClassContext?) {
        if (visitorContext.hasMemberContext()) return
        createMemberContext(node)
        super.visit(node, visitorContext.memberContext)
        visitorContext.removeMemberContext()
    }

    override fun visit(node: MethodCallExpr, arg: ClassContext?) {
        if (arg is MemberContext)
            resolver.resolve(node)
    }

    override fun visit(node: FieldAccessExpr, arg: ClassContext?) {
        if (arg is MemberContext)
            resolver.resolve(node)
    }

    override fun visit(node: VariableDeclarator, arg: ClassContext?) {
        if (arg is MemberContext) {
            arg.addLocalVariable(LocalVariableParser.instantiateLocalVariable(node))
            super.visit(node, arg)
        }
    }

    // Shorthand methods
    private fun createMemberContext(node: Node) {
        if (visitorContext.hasMemberContext()) return
        when (node) {
            is MethodDeclaration -> visitorContext.createMemberContext(
                MemberSignature(
                    MemberDeclarationSignature(node),
                    resolver.getHierarchyGraph()
                )
            )
            is ConstructorDeclaration -> visitorContext.createMemberContext(
                MemberSignature(
                    MemberDeclarationSignature(node),
                    resolver.getHierarchyGraph()
                )
            )
            else -> {}
        }
    }
}