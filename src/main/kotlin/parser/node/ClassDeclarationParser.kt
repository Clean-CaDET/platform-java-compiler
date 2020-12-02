package parser.node

import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.Node
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.type.ClassOrInterfaceType
import model.CadetClass

object ClassDeclarationParser : AbstractNodeParser() {

    /** Instantiates a [CadetClass] object with all the basic data available from its [Node] */
    fun instantiateClass(node: ClassOrInterfaceDeclaration, parent: CadetClass?): CadetClass {
        return CadetClass().apply {
            this.name = node.nameAsString
            this.fullName = node.fullyQualifiedName.get()
            this.parent = parent
        }
    }

    fun getExtendingClassesAndInterfaces(node: CompilationUnit): List<ClassOrInterfaceType> {
        getChildByType<ClassOrInterfaceDeclaration>(node)!!.apply {
            return getChildrenByType(this)
        }
    }
}