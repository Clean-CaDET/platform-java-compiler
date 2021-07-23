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
        // Parse source code
        val compilationUnits = parseAllFiles(sourceCodeList)

        // First pass (extracting skeletons)
        val unresolvedPairs = createJavaPrototypes(compilationUnits)

        // Second pass (resolving references)
        val resolvedJavaPrototypes = SymbolResolverVisitor().resolveSourceCode(unresolvedPairs)

        // Final result extraction
        return extractCadetClassesFromPrototypes(resolvedJavaPrototypes)
    }

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

    private fun createJavaPrototypes(compilationUnits: List<CompilationUnit>)
    : List<Pair<ClassOrInterfaceDeclaration, JavaPrototype>>
    {
        val prototypeVisitor = JavaPrototypeVisitor()
        val pairs = mutableListOf<Pair<ClassOrInterfaceDeclaration, JavaPrototype>>()

        for (cUnit in compilationUnits) {
            val pair = prototypeVisitor.parseCompilationUnit(cUnit)
            pairs.addAll(pair)
        }

        return pairs
    }

    private fun extractCadetClassesFromPrototypes(prototypes: List<JavaPrototype>): List<CadetClass>
    {
        val resolvedCadetClasses = mutableListOf<CadetClass>()
        prototypes.filterIsInstance<ClassPrototype>().forEach { classPrototype ->
            resolvedCadetClasses.add(classPrototype.cadetClass)
        }
        return resolvedCadetClasses
    }
}