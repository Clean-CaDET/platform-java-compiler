import com.github.javaparser.ParseProblemException
import com.github.javaparser.StaticJavaParser
import com.github.javaparser.ast.CompilationUnit
import second_pass.hierarchy.HierarchyGraph
import cadet_model.CadetClass
import second_pass.SymbolResolverVisitor
import first_pass.ClassPrototypeVisitor
import util.ResolverVisitorResource
import util.SourceCodeDto


class JavaCodeParser {
    private val hierarchyGraph = HierarchyGraph()

    private val resolverResourceList = mutableListOf<ResolverVisitorResource>()

    private val prototypeVisitor = ClassPrototypeVisitor()
    private val resolverVisitor = SymbolResolverVisitor(hierarchyGraph, resolverResourceList)

    fun parseFiles(sourceCodeDtoList: List<SourceCodeDto>): List<CadetClass>? {

        lateinit var cUnit: CompilationUnit

        for (sourceCodeDto in sourceCodeDtoList) {
            try { cUnit = StaticJavaParser.parse(sourceCodeDto.sourceCode) }
            catch (e: ParseProblemException) { return null }
            resolverResourceList.add(
                ResolverVisitorResource(
                    cUnit,
                    prototypeVisit(cUnit)
                )
            )
        }
        resolverVisitor.resolveResources()
        return hierarchyGraph.getClasses()
    }

    private fun prototypeVisit(cUnit: CompilationUnit): CadetClass {
        prototypeVisitor.parseTree(cUnit)
            .let { output ->
                hierarchyGraph.addClass(output.cadetClass)
                output.interfaces.forEach { Interface ->
                    hierarchyGraph.addInterface(Interface)
                }
                return output.cadetClass
            }
    }
}