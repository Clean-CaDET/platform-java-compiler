package second_pass.resolver.node_parser

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.expr.*
import first_pass.node_parser.AbstractNodeParser

object MethodCallExpressionParser : AbstractNodeParser() {

    // AST notes:
    // Parameters:
    // If only a name is passed: NameExpr->SimpleName
    // If function is passed as parameter: MethodCallExpr->SimpleName (and potentially other functions inside of it)
    // If a literal is passed: IntegerLiteralExpr, StringLiteralExpr, BooleanLiteralExpr, DoubleLiteralExpr, etc
    // If an object is created: ObjectCreationExpr->ClassOrInterfaceType->SimpleName

    // When chaining function, the function which is called the last will be at the top of the AST structure.
    // Example of the AST (stair-like print) for: function1().function2(arg).function3(arg);
    // MethodCallExpr (function3)
    //    MethodCallExpr (function2)
    //      MethodCallExpr (function1)
    //          ... Other methods wrapped recursively in reverse-calling order
    //    SimpleName (string = "function3")
    //    Arguments, each argument is a child, no recursive structuring for them
    // * Note that each parameter can be a separate CadetMember call, thus can have multiple levels of depth
    //   Eg. function1(function4(new ParamObject(), arg))
    //   In this case, function4 will be a direct child node to function1, but function4 will have its own
    //   children, and those children will have their own children, and so on.

    /**
     * @return Object or class name, on which the given method is called.
     * Null if the method has no explicit caller (called from its class, or a system method)
     */
    fun getCaller(node: MethodCallExpr): Node? {
        // Child nodes for MethodCallExpr are these, and in this specific order:
        // 1. Caller (can be MethodCallExpr, NameExpr, ObjectCreationExpr, FieldAccessExpr, ThisExpr, or nothing if function is local)
        // 2. SimpleName (this is the name of the function)
        // 3. Parameter[] list, where each parameter is 1 child node, with arbitrary depth, depending on which
        //    type of parameter it is (literals will have no children, MethodCallExpr will have children, etc)
        if (node.childNodes.isEmpty()) return null
        return when (val callerNode = node.childNodes[0]) {
            is SimpleName -> null
            is ThisExpr -> callerNode
            is NameExpr -> callerNode
            is FieldAccessExpr -> callerNode
            is MethodCallExpr -> callerNode
            is ObjectCreationExpr -> callerNode
            is SuperExpr -> callerNode
            is StringLiteralExpr -> callerNode
            else -> {
                println("Caller node of type ${callerNode.metaModel.typeName} not recognized as valid.")
                return null
                //throw IllegalArgumentException("Caller node of type ${callerNode.metaModel.typeName} not recognized as valid.")
            }
        }

        // Caller could be determined by the following algorithm, revolving around 'SimpleName' position in the child node list
        // a) 'SimpleName' is at position[0]
        //      -> Function is local or a system call. A simple second_pass.context lookup will determine this
        // b) 'SimpleName' is at position[1]
        //      b1) ThisExpr is at position[0]
        //          -> Local function
        //      b2) NameExpr is at position[0]
        //          -> Function is called on an object, find object name, then type, then lookup in classmap
        //          -> Function is called on a static class reference
        //      b3) FieldAccessExpr is at position[0]
        //          -> Function is called on a field of a certain object (can be arbitrary depth)
        //          * Note that this can also be static field access, singleton access etc
        //      b4) MethodCallExpr is at position[0]
        //          -> Function is called on the return value of another function (method chaining)
        //      b5) ObjectCreationExpr is at position[0]
        //      b6) ??
    }
}