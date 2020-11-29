package visitor

import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.ConstructorDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.VariableDeclarator
import com.github.javaparser.ast.expr.MethodCallExpr
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import context.Context
import context.MemberContext
import model.CadetClass
import parser.node.FieldDeclarationParser
import parser.node.MethodCallExpressionParser
import resolver.SymbolResolver
import signature.SignableMemberDeclaration
import signature.MemberSignature

class InnerVisitor(
    private val classMap: CadetClassMap,
    private val resolver: SymbolResolver
) : VoidVisitorAdapter<Context>() {

    fun parseTree(compilationUnit: CompilationUnit, cadetClass: CadetClass) {
        classMap.createClassContext(cadetClass)
        visit(compilationUnit, null)
    }

    override fun visit(node: MethodDeclaration?, arg: Context?) {
        classMap.createMemberContext(MemberSignature(SignableMemberDeclaration(node!!)))
        super.visit(node, classMap.getMemberContext())
    }

    override fun visit(node: ConstructorDeclaration?, arg: Context?) {
        classMap.createMemberContext(MemberSignature(SignableMemberDeclaration(node!!)))
        super.visit(node, classMap.getMemberContext())
    }

    override fun visit(node: MethodCallExpr?, arg: Context?) {
        arg?.let { context ->
            if (context is MemberContext) {

                val caller = MethodCallExpressionParser.getCaller(node!!)
                resolver.resolve(node, caller)
            }
        }
    }

    override fun visit(node: VariableDeclarator?, arg: Context?) {
        arg?.let { context ->
            if (context is MemberContext) {
                context.addLocalVariable(FieldDeclarationParser.instantiateLocalVariable(node!!))
            }
        }
    }
}