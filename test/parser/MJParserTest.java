package parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.antlr.v4.runtime.CharStreams;
import org.junit.Test;

import antlr.MJLexer;
import parser.ast.GoalNode;
import parser.ast.MethodDeclNode;
import parser.ast.interfaces.ExprNode;
import parser.ast.interfaces.StatementNode;

public class MJParserTest {

    @Test public void TestIntDecl() throws IOException {
        String varDecl = "int foo;";
        InputStream targetStream = new ByteArrayInputStream(varDecl.getBytes());
        MJLexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        MJParser mjParser = new MJParser(mjLexer);
        mjParser.parseVarDecl();
    }

    @Test public void TestIntArrayDecl() throws IOException {
        String varDecl = "int[] foo;";
        InputStream targetStream = new ByteArrayInputStream(varDecl.getBytes());
        MJLexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        MJParser mjParser = new MJParser(mjLexer);
        mjParser.parseVarDecl();
    }

    @Test public void TestCustomTypeDecl() throws IOException {
        String varDecl = "myType foo;";
        InputStream targetStream = new ByteArrayInputStream(varDecl.getBytes());
        MJLexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        MJParser mjParser = new MJParser(mjLexer);
        mjParser.parseVarDecl();
    }

    @Test public void TestMethodDecl() throws IOException {
        String methodDeclStr = "public int myMethod(type1 arg1, type2 arg2) {\n" + "return 2;\n" + "}";
        InputStream targetStream = new ByteArrayInputStream(methodDeclStr.getBytes());
        MJLexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        MJParser mjParser = new MJParser(mjLexer);
        MethodDeclNode methodDecl = mjParser.parseMethodDecl();
    }

    @Test public void TestArithmeticExpr() throws IOException {
        String exprStr = "2 + 3 * 4 - 7";
        InputStream targetStream = new ByteArrayInputStream(exprStr.getBytes());
        MJLexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        MJParser mjParser = new MJParser(mjLexer);
        ExprNode expr = mjParser.parseExpr();
        System.out.println(exprStr);
        System.out.println(expr.prettyPrint(""));
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~");
    }

    @Test public void TestBooleanExpr() throws IOException {
        String exprStr = "!a && b";
        InputStream targetStream = new ByteArrayInputStream(exprStr.getBytes());
        MJLexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        MJParser mjParser = new MJParser(mjLexer);
        ExprNode expr = mjParser.parseExpr();
        System.out.println(exprStr);
        System.out.println(expr.prettyPrint(""));
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~");
    }

    @Test public void TestParenthesisExpr() throws IOException {
        String exprStr = "(a + b) * c";
        InputStream targetStream = new ByteArrayInputStream(exprStr.getBytes());
        MJLexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        MJParser mjParser = new MJParser(mjLexer);
        ExprNode expr = mjParser.parseExpr();
        System.out.println(exprStr);
        System.out.println(expr.prettyPrint(""));
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~");
    }

    @Test public void TestNewArrayExpr() throws IOException {
        String exprStr = "new int[a+b]";
        InputStream targetStream = new ByteArrayInputStream(exprStr.getBytes());
        MJLexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        MJParser mjParser = new MJParser(mjLexer);
        ExprNode expr = mjParser.parseExpr();
        System.out.println(exprStr);
        System.out.println(expr.prettyPrint(""));
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~");
    }

    @Test public void TestNewObjectExpr() throws IOException {
        String exprStr = "new myObject()";
        InputStream targetStream = new ByteArrayInputStream(exprStr.getBytes());
        MJLexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        MJParser mjParser = new MJParser(mjLexer);
        ExprNode expr = mjParser.parseExpr();
        System.out.println(exprStr);
        System.out.println(expr.prettyPrint(""));
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~");
    }

    @Test public void TestArrayAccess() throws IOException {
        String exprStr = "a[b + c]";
        InputStream targetStream = new ByteArrayInputStream(exprStr.getBytes());
        MJLexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        MJParser mjParser = new MJParser(mjLexer);
        ExprNode expr = mjParser.parseExpr();
        System.out.println(exprStr);
        System.out.println(expr.prettyPrint(""));
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~");
    }

    @Test public void TestMethodCall() throws IOException {
        String exprStr = "a.foo.bar(baz)";
        InputStream targetStream = new ByteArrayInputStream(exprStr.getBytes());
        MJLexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        MJParser mjParser = new MJParser(mjLexer);
        ExprNode expr = mjParser.parseExpr();
        System.out.println(exprStr);
        System.out.println(expr.prettyPrint(""));
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~");
    }

    @Test public void TestLength() throws IOException {
        String exprStr = "a.length";
        InputStream targetStream = new ByteArrayInputStream(exprStr.getBytes());
        MJLexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        MJParser mjParser = new MJParser(mjLexer);
        ExprNode expr = mjParser.parseExpr();
        System.out.println(exprStr);
        System.out.println(expr.prettyPrint(""));
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~");
    }

    @Test public void TestSetArrayIndex() throws IOException {
        String stmtStr = "a[10] = 2;";
        InputStream targetStream = new ByteArrayInputStream(stmtStr.getBytes());
        MJLexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        MJParser mjParser = new MJParser(mjLexer);
        StatementNode stmt = mjParser.parseStatement();
        System.out.println(stmtStr);
        System.out.println(stmt.prettyPrint(""));
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~");
    }

    @Test public void TestSetVar() throws IOException {
        String stmtStr = "a = b.foo();";
        InputStream targetStream = new ByteArrayInputStream(stmtStr.getBytes());
        MJLexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        MJParser mjParser = new MJParser(mjLexer);
        StatementNode expr = mjParser.parseStatement();
        System.out.println(stmtStr);
        System.out.println(expr.prettyPrint(""));
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~");
    }

    @Test public void TestSimpleProgram() throws IOException {
        String simpleProgram = "class Main {\n" + "public static void main(String[] args) {\n"
                + "System.out.println(123);\n" + "}" + "}";
        InputStream targetStream = new ByteArrayInputStream(simpleProgram.getBytes());
        MJLexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        MJParser mjParser = new MJParser(mjLexer);
        GoalNode goal = mjParser.parseGoal();
        System.out.println(simpleProgram);
        System.out.println(goal.prettyPrint(""));
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~");
    }

    @Test public void TestClassOtherThanMain() throws IOException {
        String simpleProgram = "class Main {\n" + "public static void main(String[] args) {\n"
                + "System.out.println(4);\n" + "}\n" + "}\n" + "\n" + "class Foo {\n" + "int bar;\n"
                + "public int baz() {\n" + "return 2;\n" + "}\n" + "}\n";
        InputStream targetStream = new ByteArrayInputStream(simpleProgram.getBytes());
        MJLexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        MJParser mjParser = new MJParser(mjLexer);
        GoalNode goal = mjParser.parseGoal();
        System.out.println(simpleProgram);
        System.out.println(goal.prettyPrint(""));
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~");
    }

}
