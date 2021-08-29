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

class JavaPrototypeVisitor : VoidVisitorAdapter<JavaPrototypeVisitor.VisitorContext>() {

    class VisitorContext {
        val output: MutableList<Pair<ClassOrInterfaceDeclaration, JavaPrototype>>
        var contextClass: CadetClass? = null

        constructor() {
            this.output = mutableListOf()
        }

        private constructor(existingContext: VisitorContext, cadetClass: CadetClass) {
            this.output = existingContext.output
            this.contextClass = cadetClass
        }

        fun replicate(cadetClass: CadetClass): VisitorContext {
            return VisitorContext(this, cadetClass)
        }
    }

    fun parseCompilationUnit(compilationUnit: CompilationUnit): List<Pair<ClassOrInterfaceDeclaration, JavaPrototype>> {
        val visitorContext = VisitorContext()
        visit(compilationUnit, visitorContext)
        return visitorContext.output
    }

    override fun visit(node: ClassOrInterfaceDeclaration, ctx: VisitorContext) {
        when {
            node.isInterface -> ctx.output.add(Pair(node, InterfacePrototype(node.nameAsString)))
            else -> {
                val cadetClass = ClassDeclarationParser.instantiateClass(node, ctx.contextClass)
                val classPrototype = ClassPrototype(cadetClass)
                classPrototype.hierarchySymbols.addAll(ClassDeclarationParser.getExtendingClassesAndInterfaces(node));

                val newContext = ctx.replicate(cadetClass)
                ctx.output.add(Pair(node, classPrototype))

                super.visit(node, newContext)
            }
        }
    }

    // Enumerations are unsupported ATM
    override fun visit(node: EnumDeclaration, ctx: VisitorContext) { return }

    override fun visit(node: MethodDeclaration, ctx: VisitorContext) {
        MemberDeclarationParser.instantiateMethod(node, ctx.contextClass!!)
            .let { ctx.contextClass!!.members.add(it) }
    }

    override fun visit(node: ConstructorDeclaration, ctx: VisitorContext) {
        MemberDeclarationParser.instantiateConstructor(node, ctx.contextClass!!)
            .let { ctx.contextClass!!.members.add(it) }
    }

    override fun visit(node: FieldDeclaration, ctx: VisitorContext) {
        FieldDeclarationParser.instantiateField(node, ctx.contextClass!!)
            .let { ctx.contextClass!!.fields.add(it) }
    }
}