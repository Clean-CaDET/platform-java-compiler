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
import first_pass.prototype_dto.ClassPrototype
import first_pass.prototype_dto.InterfacePrototype
import first_pass.prototype_dto.JavaPrototype

class JavaPrototypeVisitor : VoidVisitorAdapter<CadetClass>() {

    private lateinit var output: JavaPrototype

    fun parseTree(compilationUnit: CompilationUnit): JavaPrototype {
        visit(compilationUnit, null)
        return output
    }

    override fun visit(node: ClassOrInterfaceDeclaration, arg: CadetClass?) {
        if (node.isInterface) {
            output = InterfacePrototype(node.nameAsString)
        }
        else {
            val cadetClass = ClassDeclarationParser.instantiateClass(node, arg)
            output = ClassPrototype(cadetClass)

            val symbols = ClassDeclarationParser.getExtendingClassesAndInterfaces(node)
            for (symbol in symbols)
                (output as ClassPrototype).hierarchySymbols.add(symbol.nameAsString)

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
        FieldDeclarationParser.instantiateField(node, arg!!)
            .let { arg.fields.add(it) }
    }
}