package parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.antlr.v4.runtime.CharStreams;
import org.junit.Test;

import antlr.MJLexer;

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
        String methodDecl = "public int myMethod(type1 arg1, type2 arg2) {\n" + "return 2;\n" + "}";
        InputStream targetStream = new ByteArrayInputStream(methodDecl.getBytes());
        MJLexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        MJParser mjParser = new MJParser(mjLexer);
        mjParser.parseMethodDecl();
    }

    @Test public void TestArithmeticExpr() throws IOException {
        String expr = "2 + 3 * 4 - 7";
        InputStream targetStream = new ByteArrayInputStream(expr.getBytes());
        MJLexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        MJParser mjParser = new MJParser(mjLexer);
        mjParser.parseExpr();
    }

    @Test public void TestBooleanExpr() throws IOException {
        String expr = "!a && b";
        InputStream targetStream = new ByteArrayInputStream(expr.getBytes());
        MJLexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        MJParser mjParser = new MJParser(mjLexer);
        mjParser.parseExpr();
    }

    @Test public void TestArrayAccess() throws IOException {
        String expr = "a[b + c]";
        InputStream targetStream = new ByteArrayInputStream(expr.getBytes());
        MJLexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        MJParser mjParser = new MJParser(mjLexer);
        mjParser.parseExpr();
    }

    @Test public void TestMethodCall() throws IOException {
        String expr = "a.foo.bar(baz)";
        InputStream targetStream = new ByteArrayInputStream(expr.getBytes());
        MJLexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        MJParser mjParser = new MJParser(mjLexer);
        mjParser.parseExpr();
    }

    @Test public void TestLength() throws IOException {
        String expr = "a.length";
        InputStream targetStream = new ByteArrayInputStream(expr.getBytes());
        MJLexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        MJParser mjParser = new MJParser(mjLexer);
        mjParser.parseExpr();
    }

    @Test public void TestSimpleProgram() throws IOException {
        String simpleProgram = "class Main {\n" + "public static void main(String[] args) {\n"
                + "System.out.println(123);\n" + "}" + "}";
        InputStream targetStream = new ByteArrayInputStream(simpleProgram.getBytes());
        MJLexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        MJParser mjParser = new MJParser(mjLexer);
        mjParser.parseGoal();
    }

    @Test public void TestClassOtherThanMain() throws IOException {
        String simpleProgram = "class Main {\n" + "public static void main(String[] args) {\n"
                + "System.out.println(4);\n" + "}\n" + "}\n" + "\n" + "class Foo {\n" + "int bar;\n"
                + "public int baz() {\n" + "return 2;\n" + "}\n" + "}\n";
        InputStream targetStream = new ByteArrayInputStream(simpleProgram.getBytes());
        MJLexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        MJParser mjParser = new MJParser(mjLexer);
        mjParser.parseGoal();
    }

}
