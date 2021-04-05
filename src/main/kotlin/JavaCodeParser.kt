import cadet_model.CadetClass
import com.github.javaparser.ParseProblemException
import com.github.javaparser.StaticJavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import first_pass.JavaPrototypeVisitor
import prototype_dto.ClassPrototype
import prototype_dto.JavaPrototype
import second_pass.SymbolResolverVisitor


class JavaCodeParser {

    fun parseSourceCode(sourceCodeList: List<String>): List<CadetClass> {
        val compilationUnits = parseAllFiles(sourceCodeList)
        println("Completed parsing source code.\nBeginning to extract prototypes")
        val resolverPairs = createJavaPrototypes(compilationUnits)
        println("Completed extracting prototypes. \nBeginning to resolve.")
        val resolvedJavaPrototypes = SymbolResolverVisitor().resolveSourceCode(resolverPairs)
        println("Completed resolving. \nFinishing...")
        return extractCadetClassesFromPrototypes(resolvedJavaPrototypes)
    }

    /**
     *  Parses the given list of source code java strings and returns their respective
     *  CompilationUnit objects within a list.
     *  @return List of [CompilationUnit] if parsing succeeds. Null if any of the given source code files has an error.
     *  @param [sourceCodeList] List of strings where each string should represent a single .java file
     */
    // StaticJavaParser call
    private fun parseAllFiles(sourceCodeList: List<String>): List<CompilationUnit> {
        val compilationUnits = mutableListOf<CompilationUnit>()
        for (sourceCode in sourceCodeList) {
            try {
                compilationUnits.add(StaticJavaParser.parse(sourceCode))
            }
            catch (e: ParseProblemException) {
                println("ERROR: [${e.problems}]\n${sourceCode}")
                continue
            }
        }
        return compilationUnits
    }

    /**
     *  @return List of [JavaPrototype] objects.
     *  @param [compilationUnits] List of [CompilationUnit] objects created by the parser from source code.
     */
    // OuterVisitor call
    private fun createJavaPrototypes(compilationUnits: List<CompilationUnit>)
    : List<Pair<ClassOrInterfaceDeclaration, JavaPrototype>>
    {
        val prototypeVisitor = JavaPrototypeVisitor()
        val pairs = mutableListOf<Pair<ClassOrInterfaceDeclaration, JavaPrototype>>()

        for (cUnit in compilationUnits) {
            val cuPairs = prototypeVisitor.parseCompilationUnit(cUnit)
            pairs.addAll(cuPairs)
        }

        return pairs
    }

    /**
     *  Extracts all the [ClassPrototype] objects from the given list of [JavaPrototype] objects.
     *  @return List of [ClassPrototype]
     */
    private fun extractCadetClassesFromPrototypes(prototypes: List<JavaPrototype>): List<CadetClass>
    {
        val resolvedCadetClasses = mutableListOf<CadetClass>()
        prototypes.filterIsInstance<ClassPrototype>().forEach { classPrototype ->
            resolvedCadetClasses.add(classPrototype.cadetClass)
        }
        return resolvedCadetClasses
    }
}