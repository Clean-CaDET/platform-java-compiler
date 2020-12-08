package util

import cadet_model.CadetClass
import com.github.javaparser.ast.CompilationUnit

class ResolverVisitorResource(
    val compilationUnit: CompilationUnit,
    val cadetClass: CadetClass
)