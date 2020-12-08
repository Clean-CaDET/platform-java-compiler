import cadet_model.CadetClass
import com.github.javaparser.ParseProblemException
import com.github.javaparser.StaticJavaParser
import com.github.javaparser.ast.CompilationUnit
import first_pass.ClassPrototypeVisitor
import second_pass.SymbolResolverVisitor
import hierarchy.HierarchyGraph
import util.ResolverVisitorResource
import util.SourceCodeDto


class JavaCodeParser {
    private val hierarchyGraph = HierarchyGraph()

    private val prototypeVisitor = ClassPrototypeVisitor()
    private val resolverVisitor = SymbolResolverVisitor()
    private val resolverResourceList = mutableListOf<ResolverVisitorResource>()

    fun parseFiles(sourceCodeDtoList: List<SourceCodeDto>): List<CadetClass>? {

        lateinit var cUnit: CompilationUnit

        for (sourceCodeDto in sourceCodeDtoList) {
            try {
                cUnit = StaticJavaParser.parse(sourceCodeDto.sourceCode)
            }
            catch (e: ParseProblemException) {
                return null
            }
            resolverResourceList.add(
                ResolverVisitorResource(
                    cUnit,
                    createCadetPrototype(cUnit)
                )
            )
        }
        resolverVisitor.resolveResources(hierarchyGraph, resolverResourceList)
        return hierarchyGraph.getClasses()
    }

    private fun createCadetPrototype(cUnit: CompilationUnit): CadetClass {
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