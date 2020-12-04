package second_pass.resolver.solver_nodes.cadet

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.expr.FieldAccessExpr
import cadet_model.abs.CadetVariable
import first_pass.node_parser.FieldAccessExpressionParser
import second_pass.resolver.SymbolSolvingBundle
import second_pass.resolver.solver_nodes.abs.WithCallerSolverNode

class FieldAccessSolverNode(node: FieldAccessExpr, symbolMap: SymbolSolvingBundle)
    : WithCallerSolverNode<CadetVariable>(node, symbolMap)
{
    override var caller: Node? = FieldAccessExpressionParser.getCallerNode(node)

    override fun doResolve() {
        symbolSolvingBundle.getField(
            callerResolverNode!!.returnType,
            FieldAccessExpressionParser.getVariableName(node as FieldAccessExpr)
        )?.let {
            resolvedReference = it
            returnType = it.type
        }
    }
}