package visitor

import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.ConstructorDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import model.CadetClass
import parser.node.ClassDeclarationParser
import parser.node.FieldDeclarationParser
import parser.node.MemberDeclarationParser

/**
 *  AST traversal class. Use [parseTree] to create a [CadetClass] skeleton.
 */
class OuterVisitor : VoidVisitorAdapter<CadetClass>() {

    private lateinit var parent: CadetClass

    /**
     *  Convert the AST to which the given [CompilationUnit] belongs to, into a [CadetClass] skeleton.
     *  @param compilationUnit Compilation unit at the root of the AST
     *  @return CadetClass skeleton of the given AST
     */
    fun parseTree(compilationUnit: CompilationUnit): CadetClass {
        visit(compilationUnit, null)
        return parent
    }

    // Class
    override fun visit(node: ClassOrInterfaceDeclaration?, arg: CadetClass?) {
        if (node!!.isInterface) return
        ClassDeclarationParser.instantiateClass(node, arg)   // TODO Implement inner class if arg != null
            .also {
                parent = it
                super.visit(node, parent)
            }
    }

    // Method
    override fun visit(node: MethodDeclaration?, arg: CadetClass?) {
        MemberDeclarationParser.instantiateMethod(node!!, arg!!)
            .apply {
                arg.members.add(this)
            }
    }

    // Constructor
    override fun visit(node: ConstructorDeclaration, arg: CadetClass?) {
        MemberDeclarationParser.instantiateConstructor(node, arg!!)
            .apply {
                arg.members.add(this)
            }
    }

    // Field
    override fun visit(node: FieldDeclaration, arg: CadetClass?) {
        FieldDeclarationParser.instantiateClassField(node, arg!!)
            .apply {
                arg.fields.add(this)
            }
    }
}