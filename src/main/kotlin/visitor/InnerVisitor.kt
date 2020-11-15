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
import parser.FieldDeclarationParser
import parser.MethodCallExpressionParser
import signature.SignableMemberDeclaration
import signature.MemberSignature

internal class InnerVisitor(
    private val classMap: CadetClassMap
) : VoidVisitorAdapter<Context>() {

    private lateinit var cadetClass: CadetClass

    fun parseTree(compilationUnit: CompilationUnit, cadetClass: CadetClass) {
        this.cadetClass = cadetClass
        visit(compilationUnit, null)
    }

    override fun visit(node: MethodDeclaration?, arg: Context?) {
        super.visit(node, MemberContext(
            cadetClass,
            MemberSignature(SignableMemberDeclaration(node!!))
        ))
    }

    override fun visit(node: ConstructorDeclaration?, arg: Context?) {
        super.visit(node, MemberContext(
            cadetClass,
            MemberSignature(SignableMemberDeclaration(node!!))
        ))
    }

    override fun visit(node: MethodCallExpr?, arg: Context?) {
        arg?.let { context ->
            if (context is MemberContext) {
                val caller = MethodCallExpressionParser.getCaller(node!!)
                val paramNodes = MethodCallExpressionParser.getArgumentNodes(node)

                println("${node.nameAsString}: ")
                println("\tCaller: $caller")
                println("\tParams:")
                for (paramNode in paramNodes) {
                    println("\t\t${paramNode.metaModel.typeName}")
                }

                if (caller == null) {
                    //println("Caller is implicit for '${node.nameAsString}'")
                }
                else {
                    val callerType = context.getContextScopedVariableType(caller)

                    if (callerType != null) {
                        //println("Caller type for '${node.nameAsString}' is '$callerType'")
                    }
                    else {
                        //println("'$caller' is not defined within the context scope. (At function '${node.nameAsString}')")
                    }
                }
            }
        }
    }

    override fun visit(node: VariableDeclarator?, arg: Context?) {
        arg?.let { context ->
            if (context is MemberContext) {
                context.addLocalVariable(FieldDeclarationParser.instantiateLocalField(node!!))
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