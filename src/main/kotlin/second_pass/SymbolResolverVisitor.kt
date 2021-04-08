package second_pass

import com.github.javaparser.ast.Modifier
import com.github.javaparser.ast.Node
import com.github.javaparser.ast.body.*
import com.github.javaparser.ast.expr.FieldAccessExpr
import com.github.javaparser.ast.expr.MethodCallExpr
import com.github.javaparser.ast.expr.SimpleName
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr
import com.github.javaparser.ast.type.ClassOrInterfaceType
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import prototype_dto.ClassPrototype
import prototype_dto.JavaPrototype
import second_pass.context.ClassContext
import second_pass.context.MemberContext
import second_pass.context.VisitorContext
import second_pass.resolver.SymbolResolver
import second_pass.resolver.node_parser.LocalVariableParser
import second_pass.signature.MemberDeclarationSignature
import second_pass.signature.MemberSignature
import util.AstNodeUtil
import java.lang.IllegalStateException

class SymbolResolverVisitor : VoidVisitorAdapter<ClassContext>() {

    private val visitorContext = VisitorContext()
    private lateinit var resolver: SymbolResolver

    fun resolveSourceCode(resolverPairs: List<Pair<ClassOrInterfaceDeclaration, JavaPrototype>>)
        : List<JavaPrototype>
    {
        val prototypes = isolatePrototypes(resolverPairs)
        resolver = SymbolResolver(visitorContext, prototypes)
        resolvePrototypes(resolverPairs)
        return prototypes
    }

    private fun isolatePrototypes(resolverPairs: List<Pair<ClassOrInterfaceDeclaration, JavaPrototype>>)
    : List<JavaPrototype> {
        return mutableListOf<JavaPrototype>().apply {
            resolverPairs.forEach {pair -> this.add(pair.second)}
        }
    }

    private fun resolvePrototypes(resolverPairs: List<Pair<ClassOrInterfaceDeclaration, JavaPrototype>>)
    {
        for (pair in resolverPairs) {
            if (pair.second is ClassPrototype) {
                visitorContext.createClassContext((pair.second as ClassPrototype).cadetClass)
                visitTopLevelChildren(pair.first)
            }
        }
    }

    private fun visitTopLevelChildren(node: ClassOrInterfaceDeclaration) {
        AstNodeUtil
            .getDirectChildNodes(node)
            .forEach {childNode ->
                when (childNode) {
                    is MethodDeclaration -> visit(childNode, null)
                    is ConstructorDeclaration -> visit(childNode, null)
                    is FieldDeclaration -> visit(childNode, null)
                    else -> {}
                }
            }
    }

// Overriden visitor methods
    override fun visit(node: ClassOrInterfaceDeclaration, arg: ClassContext?) {
        return
    }

    // TODO Fix this jumble with VisitorContext calls, it's ugly
    override fun visit(node: MethodDeclaration, arg: ClassContext?) {
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
        if (isMemberContext(arg))
            resolver.resolve(node)
    }

    override fun visit(node: FieldAccessExpr, arg: ClassContext?) {
        if (isMemberContext(arg))
            resolver.resolve(node)
    }

    override fun visit(node: VariableDeclarator, arg: ClassContext?) {
        if (isMemberContext(arg)) {
            (arg as MemberContext).addLocalVariable(LocalVariableParser.instantiateLocalVariable(node))
            super.visit(node, arg)
        }
    }

// Shorthand methods
    private fun createMemberContext(node: Node) {
        if (visitorContext.hasMemberContext()) return
        when (node) {
            is MethodDeclaration -> visitorContext.createMemberContext(MemberSignature(MemberDeclarationSignature(node), resolver.getHierarchyGraph()))
            is ConstructorDeclaration -> visitorContext.createMemberContext(MemberSignature(MemberDeclarationSignature(node), resolver.getHierarchyGraph()))
            else -> {}
        }
    }

    private fun isMemberContext(arg: ClassContext?): Boolean {
        arg ?: return false
        return arg is MemberContext
    }
}