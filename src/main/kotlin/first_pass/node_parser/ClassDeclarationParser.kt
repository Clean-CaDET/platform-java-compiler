package first_pass.node_parser

import cadet_model.CadetClass
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.type.ClassOrInterfaceType

object ClassDeclarationParser : AbstractNodeParser() {

    fun instantiateClass(node: ClassOrInterfaceDeclaration, parent: CadetClass?): CadetClass {
        return CadetClass().apply {
            this.name = node.nameAsString
            this.fullName = node.fullyQualifiedName.get()
            this.parent = parent
        }
    }

    fun getExtendingClassesAndInterfaces(node: ClassOrInterfaceDeclaration): List<ClassOrInterfaceType> {
            return getChildrenByType(node)
    }
}