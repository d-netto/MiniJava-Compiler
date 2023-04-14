package parser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.antlr.v4.runtime.Token;

import antlr_lexer.MJLexer;
import parser.ast.ClassNode;
import parser.ast.GoalNode;
import parser.ast.MethodDeclNode;
import parser.ast.VarDeclNode;
import parser.ast.base_abs_classes.ExprNode;
import parser.ast.base_abs_classes.StatementNode;
import parser.ast.expression.ArrayAccessExpr;
import parser.ast.expression.LengthExpr;
import parser.ast.expression.MethodCallExpr;
import parser.ast.expression.NewArrayDeclExpr;
import parser.ast.expression.NewObjectDeclExpr;
import parser.ast.expression.NotExpr;
import parser.ast.expression.binary_expr.AddExpr;
import parser.ast.expression.binary_expr.AndExpr;
import parser.ast.expression.binary_expr.DotExpr;
import parser.ast.expression.binary_expr.LtExpr;
import parser.ast.expression.binary_expr.MultExpr;
import parser.ast.expression.binary_expr.SubExpr;
import parser.ast.expression.literals.IdentifierExpr;
import parser.ast.expression.literals.IntExpr;
import parser.ast.expression.singletons.FalseExpr;
import parser.ast.expression.singletons.ThisExpr;
import parser.ast.expression.singletons.TrueExpr;
import parser.ast.statement.BlockStatement;
import parser.ast.statement.IfStatement;
import parser.ast.statement.PrintStatement;
import parser.ast.statement.SetArrayIndexStatement;
import parser.ast.statement.SetVariableStatement;
import parser.ast.statement.WhileStatement;
import utils.Pair;

public class MJParser {

  public class TokenBuffer {

    private final LinkedList<Token> buffer;
    private boolean hasReachedEnd;

    public TokenBuffer(LinkedList<Token> buffer) {
      this.buffer = buffer;
      this.hasReachedEnd = false;
    }
  }

  private final TokenBuffer tokenBuffer;
  private final MJLexer scanner;
  private static final int BUFFER_SIZE = 10;
  private static final Set<Integer> types =
      Set.of(MJLexer.INT_KW, MJLexer.BOOLEAN_KW, MJLexer.INT_ARRAY, MJLexer.ID);
  private static final String[] ruleNames = {
    "CLASS_KW",
    "PUBLIC_KW",
    "STATIC_KW",
    "VOID_KW",
    "MAIN_KW",
    "STRING_KW",
    "EXTENDS_KW",
    "LENGTH_KW",
    "INT_KW",
    "BOOLEAN_KW",
    "INT_ARRAY",
    "LPARENS",
    "RPARENS",
    "LBRACKET",
    "RBRACKET",
    "CURLY_LBRACKET",
    "CURLY_RBRACKET",
    "IF",
    "ELSE",
    "WHILE",
    "PRINTLN",
    "RETURN",
    "EQUALS",
    "AND",
    "NOT",
    "LT",
    "PLUS",
    "MINUS",
    "MULT",
    "DOT",
    "TRUE",
    "FALSE",
    "THIS",
    "NEW",
    "ID",
    "INT_LITERAL",
    "COMMA",
    "SEMI_COLON",
    "WS",
    "COMMENT"
  };

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
    assert token.getType() == expectedTypeInt
        : String.format(
            "Expected \"%s\" in line %d but got \"%s\" with text \"%s\"",
            ruleNames[expectedTypeInt - 1],
            token.getLine(),
            ruleNames[token.getType() - 1],
            token.getText());
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
    Token shouldBeEOF = nextToken();
    assert shouldBeEOF.getType() == MJLexer.EOF
        : String.format(
            "Expected end of file but got token \"%s\" with text \"%s\"",
            shouldBeEOF.getText(), ruleNames[shouldBeEOF.getType() - 1]);
    return new GoalNode(
        mainClassName.getLine(), mainClassName.getText(), argName, statement, classes);
  }

  public ClassNode parseClass() {
    handleTokenTypeCheck(MJLexer.CLASS_KW);
    Token className = handleTokenTypeCheck(MJLexer.ID);
    Optional<String> extendsFrom = Optional.empty();
    if (lookahead(1).getType() == MJLexer.EXTENDS_KW) {
      handleTokenTypeCheck(MJLexer.EXTENDS_KW);
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
    return new ClassNode(
        className.getLine(), className.getText(), extendsFrom, varDecls, methodDecls);
  }

  public VarDeclNode parseVarDecl() {
    Token type = nextToken();
    assert types.contains(type.getType())
        : String.format(
            "Expected \"int\", \"boolean\" or some identifier in line %d", type.getLine());
    Token varName = handleTokenTypeCheck(MJLexer.ID);
    handleTokenTypeCheck(MJLexer.SEMI_COLON);
    return new VarDeclNode(type.getLine(), type.getText(), varName.getText());
  }

  public MethodDeclNode parseMethodDecl() {
    handleTokenTypeCheck(MJLexer.PUBLIC_KW);
    Token methodType = nextToken();
    assert types.contains(methodType.getType())
        : String.format(
            "Expected \"int\", \"boolean\" or some identifier in line %d", methodType.getLine());
    Token methodName = handleTokenTypeCheck(MJLexer.ID);
    handleTokenTypeCheck(MJLexer.LPARENS);
    List<Pair<String, String>> methodArgs = new ArrayList<>();
    if (types.contains(lookahead(1).getType())) {
      Token argType = nextToken();
      assert types.contains(argType.getType())
          : String.format(
              "Expected \"int\", \"boolean\" or some identifier in line %d", argType.getLine());
      Token argName = handleTokenTypeCheck(MJLexer.ID);
      methodArgs.add(new Pair<>(argType.getText(), argName.getText()));
      while (lookahead(1).getType() == MJLexer.COMMA) {
        handleTokenTypeCheck(MJLexer.COMMA);
        argType = nextToken();
        assert types.contains(argType.getType())
            : String.format(
                "Expected \"int\", \"boolean\" or some identifier in line %d", argType.getLine());
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
    return new MethodDeclNode(
        methodType.getLine(),
        methodType.getText(),
        methodName.getText(),
        methodArgs,
        varDecls,
        statements,
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
        handleTokenTypeCheck(MJLexer.CURLY_RBRACKET);
        return new BlockStatement(oneAhead.getLine(), statements);
      case MJLexer.IF:
        handleTokenTypeCheck(MJLexer.LPARENS);
        ExprNode ifCondition = parseExpr();
        handleTokenTypeCheck(MJLexer.RPARENS);
        StatementNode ifBlock = parseStatement();
        handleTokenTypeCheck(MJLexer.ELSE);
        StatementNode elseBlock = parseStatement();
        return new IfStatement(oneAhead.getLine(), ifCondition, ifBlock, elseBlock);
      case MJLexer.WHILE:
        handleTokenTypeCheck(MJLexer.LPARENS);
        ExprNode whileCondition = parseExpr();
        handleTokenTypeCheck(MJLexer.RPARENS);
        StatementNode whileBlock = parseStatement();
        return new WhileStatement(oneAhead.getLine(), whileCondition, whileBlock);
      case MJLexer.PRINTLN:
        handleTokenTypeCheck(MJLexer.LPARENS);
        ExprNode printExpr = parseExpr();
        handleTokenTypeCheck(MJLexer.RPARENS);
        handleTokenTypeCheck(MJLexer.SEMI_COLON);
        return new PrintStatement(oneAhead.getLine(), printExpr);
      case MJLexer.ID:
        Token twoAhead = nextToken();
        if (twoAhead.getType() == MJLexer.EQUALS) {
          ExprNode rightHandSide = parseExpr();
          handleTokenTypeCheck(MJLexer.SEMI_COLON);
          return new SetVariableStatement(
              oneAhead.getLine(),
              new IdentifierExpr(oneAhead.getLine(), oneAhead.getText()),
              rightHandSide);
        } else if (twoAhead.getType() == MJLexer.LBRACKET) {
          ExprNode arrayExpr = parseExpr();
          handleTokenTypeCheck(MJLexer.RBRACKET);
          handleTokenTypeCheck(MJLexer.EQUALS);
          ExprNode index = parseExpr();
          handleTokenTypeCheck(MJLexer.SEMI_COLON);
          return new SetArrayIndexStatement(
              oneAhead.getLine(),
              new IdentifierExpr(oneAhead.getLine(), oneAhead.getText()),
              arrayExpr,
              index);
        }
      default:
        throw new AssertionError(
            String.format(
                "Error while trying to parse \"%s\" symbol in line %d",
                oneAhead.getText(), oneAhead.getLine()));
    }
  }

  public ExprNode parseFactor() {
    Token oneAhead = nextToken();
    ExprNode head = null;
    switch (oneAhead.getType()) {
      case MJLexer.INT_LITERAL:
        head = new IntExpr(oneAhead.getLine(), oneAhead.getText());
        break;
      case MJLexer.TRUE:
        head = new TrueExpr(oneAhead.getLine());
        break;
      case MJLexer.FALSE:
        head = new FalseExpr(oneAhead.getLine());
        break;
      case MJLexer.ID:
        head = new IdentifierExpr(oneAhead.getLine(), oneAhead.getText());
        break;
      case MJLexer.THIS:
        head = new ThisExpr(oneAhead.getLine());
        break;
      case MJLexer.NEW:
        Token twoAhead = nextToken();
        if (twoAhead.getType() == MJLexer.INT_KW) {
          handleTokenTypeCheck(MJLexer.LBRACKET);
          ExprNode size = parseExpr();
          handleTokenTypeCheck(MJLexer.RBRACKET);
          head = new NewArrayDeclExpr(oneAhead.getLine(), size);
        } else if (twoAhead.getType() == MJLexer.ID) {
          handleTokenTypeCheck(MJLexer.LPARENS);
          handleTokenTypeCheck(MJLexer.RPARENS);
          head = new NewObjectDeclExpr(oneAhead.getLine(), twoAhead.getText());
        } else {
          throw new AssertionError(
              String.format(
                  "Failed while trying to parse \"new ...\" in line %d", oneAhead.getLine()));
        }
        break;
      case MJLexer.NOT:
        ExprNode argument = parseFactor();
        head = new NotExpr(oneAhead.getLine(), argument);
        break;
      case MJLexer.LPARENS:
        head = parseExpr();
        handleTokenTypeCheck(MJLexer.RPARENS);
        break;
      default:
        throw new AssertionError(
            String.format(
                "Failed while trying to parse \"factor\" in line %d. Got \"%s\" instead\"",
                oneAhead.getLine(), oneAhead.getText()));
    }
    switch (lookahead(1).getType()) {
      case MJLexer.LBRACKET:
        handleTokenTypeCheck(MJLexer.LBRACKET);
        ExprNode index = parseExpr();
        handleTokenTypeCheck(MJLexer.RBRACKET);
        head = new ArrayAccessExpr(oneAhead.getLine(), head, index);
        break;
      case MJLexer.DOT:
        handleTokenTypeCheck(MJLexer.DOT);
        if (lookahead(1).getType() == MJLexer.LENGTH_KW) {
          head = new LengthExpr(handleTokenTypeCheck(MJLexer.LENGTH_KW).getLine(), head);
        } else if (lookahead(1).getType() == MJLexer.ID) {
          while (lookahead(2).getType() == MJLexer.DOT) {
            IdentifierExpr fieldId =
                new IdentifierExpr(
                    lookahead(1).getLine(), handleTokenTypeCheck(MJLexer.ID).getText());
            handleTokenTypeCheck(MJLexer.DOT);
            head = new DotExpr(oneAhead.getLine(), head, fieldId);
          }

          IdentifierExpr methodName =
              new IdentifierExpr(
                  lookahead(1).getLine(), handleTokenTypeCheck(MJLexer.ID).getText());
          handleTokenTypeCheck(MJLexer.LPARENS);
          List<ExprNode> args = new ArrayList<>();
          if (!(lookahead(1).getType() == MJLexer.RPARENS)) {
            args.add(parseExpr());
            while (lookahead(1).getType() == MJLexer.COMMA) {
              handleTokenTypeCheck(MJLexer.COMMA);
              args.add(parseExpr());
            }
          }
          handleTokenTypeCheck(MJLexer.RPARENS);
          head = new MethodCallExpr(oneAhead.getLine(), head, methodName, args);
        } else {
          throw new AssertionError(
              String.format(
                  "Failed while trying to parse expression of form \"A.B\" in line %d",
                  oneAhead.getLine()));
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
      head = new MultExpr(lookahead(1).getLine(), head, rightOperand);
    }
    return head;
  }

  public ExprNode parseExpr() {
    ExprNode head = parseTerm();
    while (Set.of(MJLexer.PLUS, MJLexer.MINUS, MJLexer.AND, MJLexer.LT)
        .contains(lookahead(1).getType())) {
      Token op = nextToken();
      ExprNode rightOperand = parseTerm();
      switch (op.getType()) {
        case MJLexer.PLUS:
          head = new AddExpr(op.getLine(), head, rightOperand);
          break;
        case MJLexer.MINUS:
          head = new SubExpr(op.getLine(), head, rightOperand);
          break;
        case MJLexer.AND:
          head = new AndExpr(op.getLine(), head, rightOperand);
          break;
        case MJLexer.LT:
          head = new LtExpr(op.getLine(), head, rightOperand);
          break;
      }
    }
    return head;
  }
}
