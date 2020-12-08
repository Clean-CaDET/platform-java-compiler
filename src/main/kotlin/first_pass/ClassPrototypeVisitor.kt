package first_pass

import cadet_model.CadetClass
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.ConstructorDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import first_pass.node_parser.ClassDeclarationParser
import first_pass.node_parser.FieldDeclarationParser
import first_pass.node_parser.MemberDeclarationParser

class ClassPrototypeVisitor : VoidVisitorAdapter<CadetClass>() {

    private val output = ClassPrototypeVisitorOutput()

    fun parseTree(compilationUnit: CompilationUnit): ClassPrototypeVisitorOutput {
        visit(compilationUnit, null)
        return output
    }

    override fun visit(node: ClassOrInterfaceDeclaration, arg: CadetClass?) {
        if (node.isInterface)
            output.interfaces.add(node.nameAsString)
        else {
            ClassDeclarationParser.instantiateClass(node, arg)
                .let { output.cadetClass = it }
            super.visit(node, output.cadetClass)
        }
    }

    override fun visit(node: MethodDeclaration, arg: CadetClass?) {
        MemberDeclarationParser.instantiateMethod(node, arg!!)
            .let { arg.members.add(it) }
    }

    override fun visit(node: ConstructorDeclaration, arg: CadetClass?) {
        MemberDeclarationParser.instantiateConstructor(node, arg!!)
            .let { arg.members.add(it) }
    }

    override fun visit(node: FieldDeclaration, arg: CadetClass?) {
        FieldDeclarationParser.instantiateClassField(node, arg!!)
            .let { arg.fields.add(it) }
    }
}