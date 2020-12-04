package visitor

import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.Node
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.ConstructorDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.VariableDeclarator
import com.github.javaparser.ast.expr.FieldAccessExpr
import com.github.javaparser.ast.expr.MethodCallExpr
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import context.ClassContext
import context.MemberContext
import model.CadetClass
import parser.node.FieldDeclarationParser
import resolver.SymbolContextMap
import resolver.SymbolResolver
import signature.MemberDeclarationSignature
import signature.MemberSignature

class InnerVisitor(
    private val classMap: SymbolContextMap,
    private val resolver: SymbolResolver
) : VoidVisitorAdapter<ClassContext>() {

    fun parseTree(compilationUnit: CompilationUnit, cadetClass: CadetClass) {
        classMap.createClassContext(cadetClass)
        visit(compilationUnit, null)
    }

    override fun visit(node: ClassOrInterfaceDeclaration, arg: ClassContext?) {
        if (!node.isInterface) super.visit(node, arg)
    }

    override fun visit(node: MethodDeclaration, arg: ClassContext?) {
        createMemberContext(node)
        super.visit(node, classMap.getMemberContext())
    }

    override fun visit(node: ConstructorDeclaration, arg: ClassContext?) {
        createMemberContext(node)
        super.visit(node, classMap.getMemberContext())
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
            is MethodDeclaration -> classMap.createMemberContext(MemberSignature(MemberDeclarationSignature(node)))
            is ConstructorDeclaration -> classMap.createMemberContext(MemberSignature(MemberDeclarationSignature(node)))
            else -> {}
        }
    }

    private fun isMemberContext(arg: ClassContext?): Boolean {
        arg ?: return false
        return arg is MemberContext
    }
}