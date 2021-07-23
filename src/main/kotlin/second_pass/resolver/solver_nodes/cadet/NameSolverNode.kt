package second_pass.resolver.solver_nodes.cadet

import cadet_model.abs.CadetVariable
import com.github.javaparser.ast.expr.NameExpr
import second_pass.resolver.SymbolResolver
import second_pass.resolver.solver_nodes.abs.CadetSolverNode

class NameSolverNode(
    node: NameExpr,
    resolver: SymbolResolver
) : CadetSolverNode<CadetVariable>(node, resolver) {

    override fun doResolve() {
        resolver.getWizard().getVariableInScope((node as NameExpr).nameAsString)
            .also {
                if (it != null) {
                    this.resolvedReference = it
                    this.returnType = it.type
                } else
                    returnType = node.nameAsString
            }
    }
}