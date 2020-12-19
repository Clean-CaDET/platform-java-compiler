package second_pass

import cadet_model.CadetClass
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.Node
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.ConstructorDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.VariableDeclarator
import com.github.javaparser.ast.expr.FieldAccessExpr
import com.github.javaparser.ast.expr.MethodCallExpr
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import first_pass.prototype_dto.ClassPrototype
import first_pass.prototype_dto.InterfacePrototype
import first_pass.prototype_dto.JavaPrototype
import second_pass.context.ClassContext
import second_pass.context.MemberContext
import second_pass.context.VisitorContext
import second_pass.hierarchy.HierarchyGraph
import second_pass.resolver.SymbolResolver
import second_pass.resolver.node_parser.LocalVariableParser
import second_pass.signature.MemberDeclarationSignature
import second_pass.signature.MemberSignature

class SymbolResolverVisitor : VoidVisitorAdapter<ClassContext>() {

    private val visitorContext = VisitorContext()
    private lateinit var resolver: SymbolResolver

    fun resolveSourceCode(compilationUnits: List<CompilationUnit>, prototypes: List<JavaPrototype>) {
        resolver = SymbolResolver(visitorContext, prototypes)
        resolvePrototypes(compilationUnits, prototypes)
    }

    private fun resolvePrototypes(compilationUnits: List<CompilationUnit>, prototypes: List<JavaPrototype>)
    {
        for (index in compilationUnits.indices) {
            if (prototypes[index] is ClassPrototype) {
                visitorContext.createClassContext((prototypes[index] as ClassPrototype).cadetClass)
                super.visit(compilationUnits[index], null)
            }
        }
    }

// Overriden visitor methods
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
            (arg as MemberContext).addLocalVariable(LocalVariableParser.instantiateLocalVariable(node))
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