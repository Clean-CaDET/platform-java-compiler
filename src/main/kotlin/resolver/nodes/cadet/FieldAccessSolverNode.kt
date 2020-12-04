package resolver.nodes.cadet

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.expr.FieldAccessExpr
import model.abs.CadetVariable
import parser.node.FieldAccessExpressionParser
import resolver.SymbolSolvingBundle
import resolver.nodes.abs.WithCallerSolverNode

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