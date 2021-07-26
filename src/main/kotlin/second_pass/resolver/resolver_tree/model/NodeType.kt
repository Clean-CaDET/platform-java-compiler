package second_pass.resolver.resolver_tree.model

enum class NodeType {
    Method, Constructor,
    Name, Field,
    Literal, Cast, Null, Enclosed,
    This, Super,
}