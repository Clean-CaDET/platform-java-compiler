package visitor

import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.ConstructorDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import hierarchy.HierarchyGraph
import model.CadetClass
import parser.node.ClassDeclarationParser
import parser.node.FieldDeclarationParser
import parser.node.MemberDeclarationParser

class OuterVisitor(private val hierarchyGraph: HierarchyGraph) : VoidVisitorAdapter<CadetClass>() {

    private lateinit var cadetClass: CadetClass

    fun parseTree(compilationUnit: CompilationUnit): CadetClass {
        visit(compilationUnit, null)
        return cadetClass
    }

    override fun visit(node: ClassOrInterfaceDeclaration, arg: CadetClass?) {
        if (node.isInterface)
            hierarchyGraph.addInterface(node.nameAsString)
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