package second_pass.resolver.solver_nodes.cadet

import cadet_model.abs.CadetVariable
import com.github.javaparser.ast.Node
import com.github.javaparser.ast.expr.FieldAccessExpr
import second_pass.resolver.node_parser.FieldAccessExpressionParser
import second_pass.resolver.SymbolResolver
import second_pass.resolver.solver_nodes.abs.WithCallerSolverNode

class FieldAccessSolverNode(node: FieldAccessExpr, resolver: SymbolResolver) :
    WithCallerSolverNode<CadetVariable>(node, resolver) {
    override var caller: Node? = FieldAccessExpressionParser.getCallerNode(node)

    override fun doResolve() {
        resolver.getWizard().getField(
            callerResolverNode!!.returnType,
            FieldAccessExpressionParser.getVariableName(node as FieldAccessExpr)
        )?.let {
            resolvedReference = it
            returnType = it.type
        }
    }
}