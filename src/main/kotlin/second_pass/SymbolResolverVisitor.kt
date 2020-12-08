package second_pass

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.ConstructorDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.VariableDeclarator
import com.github.javaparser.ast.expr.FieldAccessExpr
import com.github.javaparser.ast.expr.MethodCallExpr
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import second_pass.context.ClassContext
import second_pass.context.MemberContext
import second_pass.context.VisitorContext
import first_pass.node_parser.FieldDeclarationParser
import second_pass.hierarchy.HierarchyGraph
import second_pass.resolver.SymbolResolver
import second_pass.resolver.SymbolSolvingBundle
import second_pass.signature.MemberDeclarationSignature
import second_pass.signature.MemberSignature
import util.ResolverVisitorResource

class SymbolResolverVisitor(
    private val hierarchyGraph: HierarchyGraph,
    private val resources: List<ResolverVisitorResource>
) : VoidVisitorAdapter<ClassContext>() {

    private val visitorContext = VisitorContext()
    private val resolver = SymbolResolver(SymbolSolvingBundle(hierarchyGraph, visitorContext))

    fun resolveResources() {
        resolveHierarchy(resources)
        resolveSymbols(resources)
    }

    private fun resolveHierarchy(resources: List<ResolverVisitorResource>) {
        val hierarchyVisitor = HierarchyVisitor(visitorContext, hierarchyGraph)
        resources.forEach { hierarchyVisitor.parseTree(it.compilationUnit, it.cadetClass) }
    }

    private fun resolveSymbols(resources: List<ResolverVisitorResource>) {
        resources.forEach { resource ->
            visitorContext.createClassContext(resource.cadetClass)
            super.visit(resource.compilationUnit, null)
        }
    }

    override fun visit(node: ClassOrInterfaceDeclaration, arg: ClassContext?) {
        if (!node.isInterface) super.visit(node, arg)
    }

    override fun visit(node: MethodDeclaration, arg: ClassContext?) {
        createMemberContext(node)
        super.visit(node, visitorContext.memberContext)
    }

    override fun visit(node: ConstructorDeclaration, arg: ClassContext?) {
        createMemberContext(node)
        super.visit(node, visitorContext.memberContext)
    }

    override fun visit(node: MethodCallExpr, arg: ClassContext?) {
        if (isMemberContext(arg))
            resolver.resolve(node)
    }

    override fun visit(node: FieldAccessExpr, arg: ClassContext?) {
        if (isMemberContext(arg))
            resolver.resolve(node)
    }

    override fun visit(node: VariableDeclarator, arg: ClassContext?) {
        if (isMemberContext(arg)) {
            (arg as MemberContext).addLocalVariable(FieldDeclarationParser.instantiateLocalVariable(node))
            super.visit(node, arg)
        }
    }

    // Shorthand methods
    private fun createMemberContext(node: Node) {
        when (node) {
            is MethodDeclaration -> visitorContext.createMemberContext(MemberSignature(MemberDeclarationSignature(node)))
            is ConstructorDeclaration -> visitorContext.createMemberContext(MemberSignature(MemberDeclarationSignature(node)))
            else -> {}
        }
    }

    private fun isMemberContext(arg: ClassContext?): Boolean {
        arg ?: return false
        return arg is MemberContext
    }
}