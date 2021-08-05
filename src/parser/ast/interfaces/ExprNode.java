package parser.ast.interfaces;

import semantics.TypesVisitor;
import semantics.types.Type;

public interface ExprNode {

    public String prettyPrint(String string);

    public Type accept(TypesVisitor vis);

}
