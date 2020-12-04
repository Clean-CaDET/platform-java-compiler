package second_pass

import com.github.javaparser.ast.CompilationUnit
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
import cadet_model.CadetClass
import first_pass.node_parser.FieldDeclarationParser
import second_pass.resolver.SymbolResolver
import second_pass.signature.MemberDeclarationSignature
import second_pass.signature.MemberSignature

class InnerVisitor(
    private val visitorContext: VisitorContext,
    private val resolver: SymbolResolver
) : VoidVisitorAdapter<ClassContext>() {

    fun parseTree(compilationUnit: CompilationUnit, cadetClass: CadetClass) {
        visitorContext.createClassContext(cadetClass)
        visit(compilationUnit, null)
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