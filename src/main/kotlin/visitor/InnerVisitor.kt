package visitor

import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.Node
import com.github.javaparser.ast.body.ConstructorDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.VariableDeclarator
import com.github.javaparser.ast.expr.MethodCallExpr
import com.github.javaparser.ast.expr.ThisExpr
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import context.Context
import context.MemberContext
import model.CadetClass
import parser.node.FieldDeclarationParser
import parser.node.MethodCallExpressionParser
import resolver.SymbolResolver
import signature.SignableMemberDeclaration
import signature.MemberSignature

internal class InnerVisitor(
    private val classMap: CadetClassMap,
    private val resolver: SymbolResolver
) : VoidVisitorAdapter<Context>() {

    fun parseTree(compilationUnit: CompilationUnit, cadetClass: CadetClass) {
        classMap.currentClass = cadetClass
        visit(compilationUnit, null)
    }

    override fun visit(node: MethodDeclaration?, arg: Context?) {
        super.visit(node, MemberContext(
            classMap.currentClass,
            MemberSignature(SignableMemberDeclaration(node!!))
        ))
    }

    override fun visit(node: ConstructorDeclaration?, arg: Context?) {
        super.visit(node, MemberContext(
            classMap.currentClass,
            MemberSignature(SignableMemberDeclaration(node!!))
        ))
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

    private fun addLocalMemberInvocation(
        signature: MemberSignature,
        context: MemberContext
    ) {
        val method = context.getLocalMethod(signature)
        method?.let { localMember ->
            context.addInvokedMember(localMember)
        }
    }

    private fun addForeignMemberInvocation(
        callerType: String,
        signature: MemberSignature,
        context: MemberContext
    ) {
        classMap.getCadetMember(callerType, signature)
            ?.let { foreignMember ->
                context.addInvokedMember(foreignMember)
            }
    }
}