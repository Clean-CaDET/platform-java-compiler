package second_pass.infrastructure.dto

import cadet_model.CadetLocalVariable
import cadet_model.CadetMember
import com.github.javaparser.ast.Node

class ResolverContextData(
    val node: Node,
    val cadetMember: CadetMember,
    val localVariables: List<CadetLocalVariable>
)