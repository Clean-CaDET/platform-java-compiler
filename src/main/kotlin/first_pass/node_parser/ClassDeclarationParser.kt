package first_pass.node_parser

import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.type.ClassOrInterfaceType
import cadet_model.CadetClass

object ClassDeclarationParser : AbstractNodeParser() {

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