package first_pass

import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.ConstructorDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import second_pass.hierarchy.HierarchyGraph
import cadet_model.CadetClass
import first_pass.node_parser.ClassDeclarationParser
import first_pass.node_parser.FieldDeclarationParser
import first_pass.node_parser.MemberDeclarationParser

class OuterVisitor() : VoidVisitorAdapter<CadetClass>() {

    private val output = OuterVisitorOutput()
    private lateinit var cadetClass: CadetClass

    fun parseTree(compilationUnit: CompilationUnit): OuterVisitorOutput {
        visit(compilationUnit, null)
        output.cadetClass = cadetClass
        return output
    }

    override fun visit(node: ClassOrInterfaceDeclaration, arg: CadetClass?) {
        if (node.isInterface)
            output.interfaces.add(node.nameAsString)
        else {
            ClassDeclarationParser.instantiateClass(node, arg)
                .let { cadetClass = it }
            super.visit(node, cadetClass)
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