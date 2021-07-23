package first_pass

import cadet_model.CadetClass
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.*
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import first_pass.node_parser.ClassDeclarationParser
import first_pass.node_parser.FieldDeclarationParser
import first_pass.node_parser.MemberDeclarationParser
import prototype_dto.ClassPrototype
import prototype_dto.InterfacePrototype
import prototype_dto.JavaPrototype

class JavaPrototypeVisitor : VoidVisitorAdapter<CadetClass>() {

    private val output: MutableList<Pair<ClassOrInterfaceDeclaration, JavaPrototype>> = mutableListOf()

    fun parseCompilationUnit(compilationUnit: CompilationUnit): List<Pair<ClassOrInterfaceDeclaration, JavaPrototype>> {
        if (output.isNotEmpty())
            output.clear()
        visit(compilationUnit, null)
        return output
    }

    override fun visit(node: ClassOrInterfaceDeclaration, arg: CadetClass?) {
        when {
            node.isInterface -> output.add(Pair(node, InterfacePrototype(node.nameAsString)))
            else -> {
                val cadetClass = ClassDeclarationParser.instantiateClass(node, arg)
                val classPrototype = ClassPrototype(cadetClass)
                output.add(Pair(node, classPrototype))

                classPrototype.hierarchySymbols.addAll(ClassDeclarationParser.getExtendingClassesAndInterfaces(node));

                super.visit(node, cadetClass)
            }
        }
    }

    // Enumerations are unsupported ATM
    override fun visit(node: EnumDeclaration, arg: CadetClass?) { return }

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