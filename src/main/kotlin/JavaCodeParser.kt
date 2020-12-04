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
import second_pass.SymbolResolverVisitor
import first_pass.ClassPrototypeVisitor


class JavaCodeParser {
    private val hierarchyGraph = HierarchyGraph()
    private val visitorContext = VisitorContext()
    private val symbolSolvingBundle = SymbolSolvingBundle(hierarchyGraph, visitorContext)

    private val outerVisitor = ClassPrototypeVisitor()
    private val hierarchyVisitor = HierarchyVisitor(visitorContext, hierarchyGraph)
    private val innerVisitor = SymbolResolverVisitor(visitorContext, SymbolResolver(symbolSolvingBundle))

    private val classParserResourceList = mutableListOf<CadetClassParserResources>()

    fun parseFiles(sourceCodeDtoList: List<SourceCodeDto>): List<CadetClass>? {
        lateinit var cUnit: CompilationUnit

        for (sourceCodeDto in sourceCodeDtoList) {
            try { cUnit = StaticJavaParser.parse(sourceCodeDto.sourceCode) }
            catch (e: ParseProblemException) {
                println("Syntax errors at file ${sourceCodeDto.fileName}")
                return null
            }
            classParserResourceList.add(CadetClassParserResources(cUnit, outerVisit(cUnit)))
        }

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
        for (resource in classParserResourceList)
            hierarchyVisitor.parseTree(resource.compilationUnit, resource.cadetClass)
    }

    private fun innerVisit() {
        for (resource in classParserResourceList)
            innerVisitor.parseTree(resource.compilationUnit, resource.cadetClass)
    }

    private class CadetClassParserResources(
        val compilationUnit: CompilationUnit,
        val cadetClass: CadetClass
    )
}