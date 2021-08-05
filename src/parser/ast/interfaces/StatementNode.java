package parser.ast.interfaces;

import semantics.TypesVisitor;

public interface StatementNode {

    String prettyPrint(String string);

    public void accept(TypesVisitor vis);

}
