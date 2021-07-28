package parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.antlr.v4.runtime.Token;

import antlr.MJLexer;

// TODO: AST nodes --> interfaces and concrete types
public class MJParser {
    
    public class Pair<K, V> {
        
        private K first;
        private V second;
        
        public Pair(K first, V second) {
            this.first = first;
            this.second = second;
        }
        
        public K first() {
            return this.first;
        }
        
        public V second() {
            return this.second;
        }
        
    }
    
    private MJLexer scanner;
    private static Set<Integer> types = Set.of(MJLexer.INT_KW, MJLexer.BOOLEAN_KW, MJLexer.INT_ARRAY, MJLexer.ID);
    
    // TODO: implement this
    public boolean canFormVarDecl() {
        return false;
    }
    
    // TODO: implement this
    public boolean canFormMethodDecl() {
        return false;
    }
    
    // TODO: implement this
    public boolean canFormStatement() {
        return false;
    }

    public GoalNode parseGoal() {
        assert scanner.nextToken().getType() == MJLexer.CLASS_KW;
        Token mainClassId = scanner.nextToken();
        assert mainClassId.getType() == MJLexer.ID;
        assert scanner.nextToken().getType() == MJLexer.CURLY_LBRACKET;
        assert scanner.nextToken().getType() == MJLexer.PUBLIC_KW;
        assert scanner.nextToken().getType() == MJLexer.STATIC_KW;
        assert scanner.nextToken().getType() == MJLexer.VOID_KW;
        assert scanner.nextToken().getType() == MJLexer.VOID_KW;
        assert scanner.nextToken().getType() == MJLexer.MAIN_KW;
        assert scanner.nextToken().getType() == MJLexer.VOID_KW;
        assert scanner.nextToken().getType() == MJLexer.LPARENS;
        assert scanner.nextToken().getType() == MJLexer.VOID_KW;
        assert scanner.nextToken().getType() == MJLexer.STRING_KW;
        assert scanner.nextToken().getType() == MJLexer.LBRACKET;
        assert scanner.nextToken().getType() == MJLexer.RBRACKET;
        Token argId = scanner.nextToken();
        assert argId.getType() == MJLexer.ID;
        assert scanner.nextToken().getType() == MJLexer.CURLY_LBRACKET;
        StatementNode stmt = parseStatement();
        assert scanner.nextToken().getType() == MJLexer.CURLY_RBRACKET;
        assert scanner.nextToken().getType() == MJLexer.CURLY_RBRACKET;
        return new GoalNode(mainClassId, argId, stmt);
    }
    
    public ClassNode parseClass() {
        assert scanner.nextToken().getType() == MJLexer.CLASS_KW;
        Token className = scanner.nextToken();
        assert className.getType() == MJLexer.ID;
        Optional<Token> extendsFrom = Optional.empty();
        if (scanner.getToken().getType() == MJLexer.EXTENDS_KW) {
            extendsFrom = Optional.of(scanner.nextToken());
        }
        assert scanner.nextToken().getType() == MJLexer.CURLY_LBRACKET;
        List<VarDeclNode> varDecls = new ArrayList<>();
        List<StatementNode> methodDecls = new ArrayList<>();
        while (canFormVarDecl()) {
            varDecls.add(parseVarDecl());
        }
        while (canFormMethodDecl()) {
            methodDecls.add(parseMethodDecl());
        }
        return ClassNode(className, extendsFrom, varDecls, methodDecls);
    }
    
    public MethodDeclNode parseMethodDecl() {
        assert scanner.nextToken().getType() == MJLexer.PUBLIC_KW;
        Token methodType = scanner.nextToken();
        assert types.contains(methodType.getType());
        Token methodName = scanner.nextToken();
        assert methodName.getType() == MJLexer.ID;
        assert scanner.nextToken().getType() == MJLexer.LPARENS;
        assert types.contains(methodType.getType());
        List<Pair<Token, Token>> methodArgs = new ArrayList<>();
        if (types.contains(scanner.getToken().getType())) {
            methodArgs.add(new Pair<>(scanner.nextToken(), scanner.nextToken()));
        }
        while (scanner.getToken().getType() == MJLexer.COMMA) {
            methodArgs.add(new Pair<>(scanner.nextToken(), scanner.nextToken()));
        }
        assert scanner.nextToken().getType() == MJLexer.RPARENS;
        assert scanner.nextToken().getType() == MJLexer.CURLY_LBRACKET;
        List<VarDeclNode> varDecls = new ArrayList<>();
        List<StatementNode> statements = new ArrayList<>();
        while (canFormVarDecl()) {
            varDecls.add(parseVarDecl());
        }
        while (canFormStatement()) {
            statements.add(parseStatement());
        }
        assert scanner.nextToken().getType() == MJLexer.RETURN;
        Expression returnExpr = parseExpr();
        assert scanner.nextToken().getType() == MJLexer.CURLY_LBRACKET;
        assert scanner.nextToken().getType() == MJLexer.SEMI_COLON;
        return ClassNode(methodType, methodName, methodArgs, varDecls, statements);
    }
    
    // TODO: implement this
    public StatementNode parseStatement() {
        return null;
    }
    
    //TODO: implement this
    public ExprNode parseExpr() {
        return null;
    }
    
}
