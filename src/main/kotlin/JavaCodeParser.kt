import cadet_model.CadetClass
import com.github.javaparser.ParseProblemException
import com.github.javaparser.StaticJavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import first_pass.JavaPrototypeVisitor
import kotlinx.coroutines.runBlocking
import prototype_dto.ClassPrototype
import prototype_dto.JavaPrototype
import second_pass.SymbolResolverVisitor
import util.Threading
import java.util.*
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis


class JavaCodeParser {
    fun parseSourceCode(sourceCodeList: List<String>): List<CadetClass> {
        // Parse source code
        lateinit var compilationUnits: List<CompilationUnit>
        val parseTime = measureTimeMillis {
            compilationUnits = parseAllFiles(sourceCodeList)
        }

        // First pass (extracting skeletons)
        lateinit var unresolvedPairs: List<Pair<ClassOrInterfaceDeclaration, JavaPrototype>>
        val firstPassTime = measureTimeMillis {
            unresolvedPairs = createJavaPrototypes(compilationUnits)
        }

        // Second pass (resolving references)
        lateinit var resolvedJavaPrototypes: List<JavaPrototype>
        val secondPassTime = measureTimeMillis {
            resolvedJavaPrototypes = SymbolResolverVisitor().resolveSourceCode(unresolvedPairs)
        }

        println("\nParse: $parseTime\n1st pass: $firstPassTime\n2nd pass: $secondPassTime")

        // Final result extraction
        return extractCadetClassesFromPrototypes(resolvedJavaPrototypes)
    }

    private fun parseAllFiles(sourceCodeList: List<String>): List<CompilationUnit> = runBlocking {
//        val compilationUnits = mutableListOf<CompilationUnit>()
//
//        for (sourceCode in sourceCodeList) {
//            try {
//                compilationUnits.add(StaticJavaParser.parse(sourceCode))
//            }
//            catch (e: ParseProblemException) {
//                // println("ERROR: [${e.problems}]\n${sourceCode}")
//                continue
//            }
//        }
//        return@runBlocking compilationUnits
//
        val compilationUnits = Collections.synchronizedList(mutableListOf<CompilationUnit>())

        Threading.iterateListSlicesViaCoroutines(
            sourceCodeList,
            function = {
                try {
                    compilationUnits.add(StaticJavaParser.parse(it))
                }
                catch (ignore: ParseProblemException) {}
            }
        )

        return@runBlocking compilationUnits
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