import cadet_model.CadetClass
import com.github.javaparser.ParseProblemException
import com.github.javaparser.StaticJavaParser
import com.github.javaparser.ast.CompilationUnit
import first_pass.JavaPrototypeVisitor
import first_pass.prototype_dto.ClassPrototype
import first_pass.prototype_dto.JavaPrototype
import second_pass.SymbolResolverVisitor


class JavaCodeParser {

    fun parseSourceCode(sourceCodeList: List<String>): List<CadetClass>? {
        val compilationUnits = parseAllFiles(sourceCodeList)
        compilationUnits ?: return null

        val javaPrototypes = createJavaPrototypes(compilationUnits)
        SymbolResolverVisitor().resolveSourceCode(compilationUnits, javaPrototypes)

        return extractCadetClassesFromPrototypes(javaPrototypes)
    }

    private fun parseAllFiles(sourceCodeList: List<String>): List<CompilationUnit>? {
        val compilationUnits = mutableListOf<CompilationUnit>()
        for (sourceCode in sourceCodeList) {
            try {
                compilationUnits.add(StaticJavaParser.parse(sourceCode))
            }
            catch (e: ParseProblemException) { return null }
        }
        return compilationUnits
    }

    private fun createJavaPrototypes(compilationUnits: List<CompilationUnit>): List<JavaPrototype> {
        val prototypeVisitor = JavaPrototypeVisitor()
        val javaPrototypes = mutableListOf<JavaPrototype>()

        for (cUnit in compilationUnits) {
            javaPrototypes.add(prototypeVisitor.parseTree(cUnit))
        }

        return javaPrototypes
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