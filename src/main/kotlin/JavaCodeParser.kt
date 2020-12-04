import com.github.javaparser.ParseProblemException
import com.github.javaparser.StaticJavaParser
import com.github.javaparser.ast.CompilationUnit
import second_pass.context.VisitorContext
import second_pass.hierarchy.HierarchyGraph
import cadet_model.CadetClass
import second_pass.resolver.SymbolSolvingBundle
import second_pass.resolver.SymbolResolver
import second_pass.signature.MemberSignature
import second_pass.HierarchyVisitor
import second_pass.InnerVisitor
import first_pass.OuterVisitor


class JavaCodeParser {
    private val hierarchyGraph = HierarchyGraph()
    private val visitorContext = VisitorContext()
    private val symbolSolvingBundle = SymbolSolvingBundle(hierarchyGraph, visitorContext)

    private val outerVisitor = OuterVisitor()
    private val hierarchyVisitor = HierarchyVisitor(visitorContext, hierarchyGraph)
    private val innerVisitor = InnerVisitor(visitorContext, SymbolResolver(symbolSolvingBundle))

    private val classParserBundleList = mutableListOf<CadetClassParserBundle>()

    fun parseFiles(sourceCodeDtoList: List<SourceCodeDto>): List<CadetClass>? {
        lateinit var cUnit: CompilationUnit

        for (sourceCodeDto in sourceCodeDtoList) {
            try { cUnit = parse(sourceCodeDto.sourceCode) }
            catch (e: ParseProblemException) {
                println("Syntax errors at file ${sourceCodeDto.fileName}")
                return null
            }
            classParserBundleList.add(CadetClassParserBundle(cUnit, outerVisit(cUnit)))
        }

        MemberSignature.injectHierarchyGraph(hierarchyGraph)
        resolveHierarchy()
        innerVisit()

        return hierarchyGraph.getClasses()
    }

    private fun outerVisit(cUnit: CompilationUnit): CadetClass {
        outerVisitor.parseTree(cUnit)
            .let { output ->
                hierarchyGraph.addClass(output.cadetClass)
                output.interfaces.forEach { Interface ->
                    hierarchyGraph.addInterface(Interface)
                }
                return output.cadetClass
            }
    }

    private fun resolveHierarchy() {
        for (classBundle in classParserBundleList)
            hierarchyVisitor.parseTree(classBundle.compilationUnit, classBundle.cadetClass)
    }

    private fun innerVisit() {
        for (classBundle in classParserBundleList)
            innerVisitor.parseTree(classBundle.compilationUnit, classBundle.cadetClass)
    }

    private class CadetClassParserBundle(
        val compilationUnit: CompilationUnit,
        val cadetClass: CadetClass
    ) {}


// JavaParser method
    private fun parse(sourceCode: String): CompilationUnit {
        return StaticJavaParser.parse(sourceCode)
    }
}