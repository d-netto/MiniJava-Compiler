package parser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.antlr.v4.runtime.Token;

import antlr.MJLexer;
import parser.ast.ClassNode;
import parser.ast.GoalNode;
import parser.ast.MethodDeclNode;
import parser.ast.VarDeclNode;
import parser.ast.expression.ArrayAccessExpr;
import parser.ast.expression.IntExpr;
import parser.ast.expression.MethodCallExpr;
import parser.ast.expression.NewArrayDeclExpr;
import parser.ast.expression.NewObjectDeclExpr;
import parser.ast.expression.binary_expr.AddExpr;
import parser.ast.expression.binary_expr.AndExpr;
import parser.ast.expression.binary_expr.DotExpr;
import parser.ast.expression.binary_expr.LtExpr;
import parser.ast.expression.binary_expr.MultExpr;
import parser.ast.expression.binary_expr.SubExpr;
import parser.ast.expression.literals.IdentifierExpr;
import parser.ast.expression.literals.LengthExpr;
import parser.ast.expression.singletons.FalseExpr;
import parser.ast.expression.singletons.NotExpr;
import parser.ast.expression.singletons.ThisExpr;
import parser.ast.expression.singletons.TrueExpr;
import parser.ast.interfaces.ExprNode;
import parser.ast.interfaces.StatementNode;
import parser.ast.statement.SetArrayIndexStatement;
import parser.ast.statement.BlockStatement;
import parser.ast.statement.IfStatement;
import parser.ast.statement.PrintStatement;
import parser.ast.statement.SetVariableStatement;
import parser.ast.statement.WhileStatement;

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

    public class TokenBuffer {

        private LinkedList<Token> buffer;
        private boolean hasReachedEnd;

        public TokenBuffer(LinkedList<Token> buffer) {
            this.buffer = buffer;
            this.hasReachedEnd = false;
        }
    }

    private final TokenBuffer tokenBuffer;
    private final MJLexer scanner;
    private static final int BUFFER_SIZE = 10;
    private static final Set<Integer> types = Set.of(MJLexer.INT_KW, MJLexer.BOOLEAN_KW, MJLexer.INT_ARRAY, MJLexer.ID);
    private static final String[] ruleNames = { "CLASS_KW", "PUBLIC_KW", "STATIC_KW", "VOID_KW", "MAIN_KW", "STRING_KW",
            "EXTENDS_KW", "LENGTH_KW", "INT_KW", "BOOLEAN_KW", "INT_ARRAY", "LPARENS", "RPARENS", "LBRACKET",
            "RBRACKET", "CURLY_LBRACKET", "CURLY_RBRACKET", "IF", "ELSE", "WHILE", "PRINTLN", "RETURN", "EQUALS", "AND",
            "NOT", "LT", "PLUS", "MINUS", "MULT", "DOT", "TRUE", "FALSE", "THIS", "NEW", "ID", "INT_LITERAL", "COMMA",
            "SEMI_COLON", "WS", "COMMENT" };

    public MJParser(MJLexer scanner) {
        LinkedList<Token> buffer = new LinkedList<>(List.of(scanner.nextToken()));
        this.tokenBuffer = new TokenBuffer(buffer);
        this.scanner = scanner;
    }

    private Token lookahead(int stepsAhead) {
        return tokenBuffer.buffer.get(stepsAhead - 1);
    }

    private Token nextToken() {
        if (!tokenBuffer.hasReachedEnd) {
            Token firstToken = tokenBuffer.buffer.remove(0);
            while (!tokenBuffer.hasReachedEnd && tokenBuffer.buffer.size() < BUFFER_SIZE) {
                Token nextToken = scanner.nextToken();
                tokenBuffer.buffer.add(nextToken);
                tokenBuffer.hasReachedEnd = (nextToken.getType() == MJLexer.EOF);
            }
            return firstToken;
        }
        return tokenBuffer.buffer.remove(0);
    }

    private boolean canFormVarDecl() {
        Token oneAhead = lookahead(1);
        Token twoAhead = lookahead(2);
        return types.contains(oneAhead.getType()) && twoAhead.getType() == MJLexer.ID;
    }

    private boolean canFormMethodDecl() {
        return lookahead(1).getType() == MJLexer.PUBLIC_KW;
    }

    private boolean canFormStatement() {
        Token oneAhead = lookahead(1);
        switch (oneAhead.getType()) {
        case MJLexer.CURLY_LBRACKET:
        case MJLexer.IF:
        case MJLexer.WHILE:
        case MJLexer.PRINTLN:
            return true;
        case MJLexer.ID:
            int twoAheadType = lookahead(2).getType();
            return twoAheadType == MJLexer.EQUALS || twoAheadType == MJLexer.LBRACKET;
        default:
            return false;
        }
    }

    private Token handleTokenTypeCheck(int expectedTypeInt) {
        Token token = nextToken();
        assert token.getType() == expectedTypeInt : String.format("Expected \"%s\" in line %d",
                ruleNames[expectedTypeInt - 1], token.getLine());
        return token;
    }

    public GoalNode parseGoal() {
        handleTokenTypeCheck(MJLexer.CLASS_KW);
        Token mainClassName = handleTokenTypeCheck(MJLexer.ID);
        handleTokenTypeCheck(MJLexer.CURLY_LBRACKET);
        handleTokenTypeCheck(MJLexer.PUBLIC_KW);
        handleTokenTypeCheck(MJLexer.STATIC_KW);
        handleTokenTypeCheck(MJLexer.VOID_KW);
        handleTokenTypeCheck(MJLexer.MAIN_KW);
        handleTokenTypeCheck(MJLexer.LPARENS);
        handleTokenTypeCheck(MJLexer.STRING_KW);
        handleTokenTypeCheck(MJLexer.LBRACKET);
        handleTokenTypeCheck(MJLexer.RBRACKET);
        String argName = handleTokenTypeCheck(MJLexer.ID).getText();
        handleTokenTypeCheck(MJLexer.RPARENS);
        handleTokenTypeCheck(MJLexer.CURLY_LBRACKET);
        StatementNode statement = parseStatement();
        handleTokenTypeCheck(MJLexer.CURLY_RBRACKET);
        handleTokenTypeCheck(MJLexer.CURLY_RBRACKET);
        List<ClassNode> classes = new ArrayList<>();
        while (lookahead(1).getType() == MJLexer.CLASS_KW) {
            classes.add(parseClass());
        }
        assert nextToken().getType() == MJLexer.EOF : "Expected end of file";
        return new GoalNode(mainClassName.getText(), argName, statement, classes);
    }

    public ClassNode parseClass() {
        handleTokenTypeCheck(MJLexer.CLASS_KW);
        Token className = handleTokenTypeCheck(MJLexer.ID);
        Optional<String> extendsFrom = Optional.empty();
        if (scanner.getToken().getType() == MJLexer.EXTENDS_KW) {
            extendsFrom = Optional.of(handleTokenTypeCheck(MJLexer.ID).getText());
        }
        handleTokenTypeCheck(MJLexer.CURLY_LBRACKET);
        List<VarDeclNode> varDecls = new ArrayList<>();
        List<MethodDeclNode> methodDecls = new ArrayList<>();
        while (canFormVarDecl()) {
            varDecls.add(parseVarDecl());
        }
        while (canFormMethodDecl()) {
            methodDecls.add(parseMethodDecl());
        }
        handleTokenTypeCheck(MJLexer.CURLY_RBRACKET);
        return new ClassNode(className.getText(), extendsFrom, varDecls, methodDecls);
    }

    public VarDeclNode parseVarDecl() {
        Token type = nextToken();
        assert types.contains(type.getType()) : "Expected \"int\", \"boolean\" or some identifier in line "
                + type.getLine();
        Token varName = handleTokenTypeCheck(MJLexer.ID);
        handleTokenTypeCheck(MJLexer.SEMI_COLON);
        return new VarDeclNode(type.getLine(), type.getText(), varName.getText());
    }

    public MethodDeclNode parseMethodDecl() {
        handleTokenTypeCheck(MJLexer.PUBLIC_KW);
        Token methodType = nextToken();
        assert types.contains(methodType.getType()) : "Expected \"int\", \"boolean\" or some identifier in line "
                + methodType.getLine();
        Token methodName = handleTokenTypeCheck(MJLexer.ID);
        handleTokenTypeCheck(MJLexer.LPARENS);
        List<Pair<String, String>> methodArgs = new ArrayList<>();
        if (types.contains(lookahead(1).getType())) {
            Token argType = nextToken();
            assert types.contains(argType.getType()) : "Expected \"int\", \"boolean\" or some identifier in line "
                    + argType.getLine();
            Token argName = handleTokenTypeCheck(MJLexer.ID);
            methodArgs.add(new Pair<>(argType.getText(), argName.getText()));
            while (lookahead(1).getType() == MJLexer.COMMA) {
                handleTokenTypeCheck(MJLexer.COMMA);
                argType = nextToken();
                assert types.contains(argType.getType()) : "Expected \"int\", \"boolean\" or some identifier in line "
                        + argType.getLine();
                argName = handleTokenTypeCheck(MJLexer.ID);
                methodArgs.add(new Pair<>(argType.getText(), argName.getText()));
            }
        }
        handleTokenTypeCheck(MJLexer.RPARENS);
        handleTokenTypeCheck(MJLexer.CURLY_LBRACKET);
        List<VarDeclNode> varDecls = new ArrayList<>();
        List<StatementNode> statements = new ArrayList<>();
        while (canFormVarDecl()) {
            varDecls.add(parseVarDecl());
        }
        while (canFormStatement()) {
            statements.add(parseStatement());
        }
        handleTokenTypeCheck(MJLexer.RETURN);
        ExprNode returnExpr = parseExpr();
        handleTokenTypeCheck(MJLexer.SEMI_COLON);
        handleTokenTypeCheck(MJLexer.CURLY_RBRACKET);
        return new MethodDeclNode(methodType.getText(), methodName.getText(), methodArgs, varDecls, statements,
                returnExpr);
    }

    public StatementNode parseStatement() {
        Token oneAhead = nextToken();
        switch (oneAhead.getType()) {
        case MJLexer.CURLY_LBRACKET:
            List<StatementNode> statements = new ArrayList<>();
            while (canFormStatement()) {
                statements.add(parseStatement());
            }
            return new BlockStatement(statements);
        case MJLexer.IF:
            handleTokenTypeCheck(MJLexer.LPARENS);
            ExprNode ifCondition = parseExpr();
            handleTokenTypeCheck(MJLexer.RPARENS);
            StatementNode ifBlock = parseStatement();
            handleTokenTypeCheck(MJLexer.ELSE);
            StatementNode elseBlock = parseStatement();
            return new IfStatement(ifCondition, ifBlock, elseBlock);
        case MJLexer.WHILE:
            handleTokenTypeCheck(MJLexer.LPARENS);
            ExprNode whileCondition = parseExpr();
            handleTokenTypeCheck(MJLexer.RPARENS);
            StatementNode whileBlock = parseStatement();
            return new WhileStatement(whileCondition, whileBlock);
        case MJLexer.PRINTLN:
            handleTokenTypeCheck(MJLexer.LPARENS);
            ExprNode printExpr = parseExpr();
            handleTokenTypeCheck(MJLexer.RPARENS);
            handleTokenTypeCheck(MJLexer.SEMI_COLON);
            return new PrintStatement(printExpr);
        case MJLexer.ID:
            Token twoAhead = nextToken();
            if (twoAhead.getType() == MJLexer.EQUALS) {
                ExprNode rhs = parseExpr();
                return new SetVariableStatement(twoAhead.getLine(), twoAhead.getText(), rhs);
            } else if (twoAhead.getType() == MJLexer.LBRACKET) {
                ExprNode arrayExpr = parseExpr();
                handleTokenTypeCheck(MJLexer.RBRACKET);
                ExprNode rhs = parseExpr();
                return new SetArrayIndexStatement(twoAhead.getLine(), twoAhead.getText(), arrayExpr, rhs);
            }
        default:
            throw new RuntimeException(
                    "Error while trying to parse " + oneAhead.getText() + " symbol in line " + oneAhead.getLine());

        }
    }

    public ExprNode parseFactor() {
        Token oneAhead = nextToken();
        ExprNode head = null;
        switch (oneAhead.getType()) {
        case MJLexer.INT_LITERAL:
            head = new IntExpr(oneAhead.getText());
            break;
        case MJLexer.TRUE:
            head = new TrueExpr();
            break;
        case MJLexer.FALSE:
            head = new FalseExpr();
            break;
        case MJLexer.ID:
            head = new IdentifierExpr(oneAhead.getText());
            break;
        case MJLexer.THIS:
            head = new ThisExpr();
            break;
        case MJLexer.NEW:
            Token twoAhead = nextToken();
            if (twoAhead.getType() == MJLexer.INT_KW) {
                handleTokenTypeCheck(MJLexer.LBRACKET);
                ExprNode size = parseExpr();
                handleTokenTypeCheck(MJLexer.RBRACKET);
                head = new NewArrayDeclExpr(size);
            } else if (twoAhead.getType() == MJLexer.ID) {
                handleTokenTypeCheck(MJLexer.LPARENS);
                handleTokenTypeCheck(MJLexer.RPARENS);
                head = new NewObjectDeclExpr(twoAhead.getText());
            }
            break;
        case MJLexer.NOT:
            ExprNode argument = parseExpr();
            head = new NotExpr(argument);
            break;
        case MJLexer.LPARENS:
            handleTokenTypeCheck(MJLexer.LPARENS);
            head = parseExpr();
            handleTokenTypeCheck(MJLexer.RPARENS);
            break;
        default:
            throw new RuntimeException("Failed while trying to parse \"factor\" in line " + oneAhead.getLine());
        }
        switch (lookahead(1).getType()) {
        case MJLexer.LBRACKET:
            handleTokenTypeCheck(MJLexer.LBRACKET);
            ExprNode index = parseExpr();
            handleTokenTypeCheck(MJLexer.RBRACKET);
            head = new ArrayAccessExpr(head, index);
            break;
        case MJLexer.DOT:
            handleTokenTypeCheck(MJLexer.DOT);
            Token nextToken = nextToken();
            if (nextToken.getType() == MJLexer.LENGTH_KW) {
                head = new LengthExpr(head);
            } else if (nextToken.getType() == MJLexer.ID) {
                ExprNode fieldId = new IdentifierExpr(nextToken.getText());
                head = new DotExpr(head, fieldId);
                while (lookahead(1).getType() == MJLexer.DOT) {
                    handleTokenTypeCheck(MJLexer.DOT);
                    fieldId = new IdentifierExpr(handleTokenTypeCheck(MJLexer.ID).getText());
                    head = new DotExpr(head, fieldId);
                }
                handleTokenTypeCheck(MJLexer.LPARENS);
                List<ExprNode> args = new ArrayList<>();
                args.add(parseExpr());
                while (lookahead(1).getType() == MJLexer.COMMA) {
                    handleTokenTypeCheck(MJLexer.COMMA);
                    args.add(parseExpr());
                }
                handleTokenTypeCheck(MJLexer.RPARENS);
                head = new MethodCallExpr(head, args);
            }
            break;
        }
        return head;
    }

    public ExprNode parseTerm() {
        ExprNode head = parseFactor();
        while (lookahead(1).getType() == MJLexer.MULT) {
            handleTokenTypeCheck(MJLexer.MULT);
            ExprNode rightOperand = parseFactor();
            head = new MultExpr(head, rightOperand);
        }
        return head;
    }

    public ExprNode parseExpr() {
        ExprNode head = parseTerm();
        while (Set.of(MJLexer.PLUS, MJLexer.MINUS, MJLexer.AND, MJLexer.LT).contains(lookahead(1).getType())) {
            Token op = nextToken();
            ExprNode rightOperand = parseTerm();
            switch (op.getType()) {
            case MJLexer.PLUS:
                head = new AddExpr(head, rightOperand);
                break;
            case MJLexer.MINUS:
                head = new SubExpr(head, rightOperand);
                break;
            case MJLexer.AND:
                head = new AndExpr(head, rightOperand);
                break;
            case MJLexer.LT:
                head = new LtExpr(head, rightOperand);
                break;
            }
        }
        return head;
    }

}
