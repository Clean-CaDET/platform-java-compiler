package second_pass.resolver.solver_nodes.cadet

import com.github.javaparser.ast.expr.NameExpr
import cadet_model.abs.CadetVariable
import second_pass.resolver.SymbolSolvingBundle
import second_pass.resolver.solver_nodes.abs.CadetSolverNode

class NameSolverNode(
    node: NameExpr,
    symbolSolvingBundle: SymbolSolvingBundle
): CadetSolverNode<CadetVariable>(node, symbolSolvingBundle) {

    override fun doResolve() {
        symbolSolvingBundle.getVariableInScope((node as NameExpr).nameAsString)
        .also {
            if (it != null) {
                this.resolvedReference = it
                this.returnType = it.type
            }
            else
                returnType = node.nameAsString
        }
    }
}