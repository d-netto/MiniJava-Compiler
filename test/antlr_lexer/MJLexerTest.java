package antlr_lexer;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Lexer;
import org.junit.Test;

public class MJLexerTest {

    @Test public void TestClassKW() throws IOException {
        String classString = "class";
        InputStream targetStream = new ByteArrayInputStream(classString.getBytes());
        Lexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        assertEquals(MJLexer.CLASS_KW, mjLexer.nextToken().getType());
    }

    @Test public void TestPublicKW() throws IOException {
        String classString = "public";
        InputStream targetStream = new ByteArrayInputStream(classString.getBytes());
        Lexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        assertEquals(MJLexer.PUBLIC_KW, mjLexer.nextToken().getType());
    }

    @Test public void TestStaticKW() throws IOException {
        String classString = "static";
        InputStream targetStream = new ByteArrayInputStream(classString.getBytes());
        Lexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        assertEquals(MJLexer.STATIC_KW, mjLexer.nextToken().getType());
    }

    @Test public void TestVoidKW() throws IOException {
        String classString = "void";
        InputStream targetStream = new ByteArrayInputStream(classString.getBytes());
        Lexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        assertEquals(MJLexer.VOID_KW, mjLexer.nextToken().getType());
    }

    @Test public void TestMainKW() throws IOException {
        String classString = "main";
        InputStream targetStream = new ByteArrayInputStream(classString.getBytes());
        Lexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        assertEquals(MJLexer.MAIN_KW, mjLexer.nextToken().getType());
    }

    @Test public void TestStringKW() throws IOException {
        String classString = "String";
        InputStream targetStream = new ByteArrayInputStream(classString.getBytes());
        Lexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        assertEquals(MJLexer.STRING_KW, mjLexer.nextToken().getType());
    }

    @Test public void TestIntKW() throws IOException {
        String classString = "int";
        InputStream targetStream = new ByteArrayInputStream(classString.getBytes());
        Lexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        assertEquals(MJLexer.INT_KW, mjLexer.nextToken().getType());
    }

    @Test public void TestBooleanKW() throws IOException {
        String classString = "boolean";
        InputStream targetStream = new ByteArrayInputStream(classString.getBytes());
        Lexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        assertEquals(MJLexer.BOOLEAN_KW, mjLexer.nextToken().getType());
    }

    @Test public void TestLParens() throws IOException {
        String classString = "(";
        InputStream targetStream = new ByteArrayInputStream(classString.getBytes());
        Lexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        assertEquals(MJLexer.LPARENS, mjLexer.nextToken().getType());
    }

    @Test public void TestRParens() throws IOException {
        String classString = ")";
        InputStream targetStream = new ByteArrayInputStream(classString.getBytes());
        Lexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        assertEquals(MJLexer.RPARENS, mjLexer.nextToken().getType());
    }

    @Test public void TestLBracket() throws IOException {
        String classString = "[";
        InputStream targetStream = new ByteArrayInputStream(classString.getBytes());
        Lexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        assertEquals(MJLexer.LBRACKET, mjLexer.nextToken().getType());
    }

    @Test public void TestRBracket() throws IOException {
        String classString = "]";
        InputStream targetStream = new ByteArrayInputStream(classString.getBytes());
        Lexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        assertEquals(MJLexer.RBRACKET, mjLexer.nextToken().getType());
    }

    @Test public void TestCurlyLBracket() throws IOException {
        String classString = "{";
        InputStream targetStream = new ByteArrayInputStream(classString.getBytes());
        Lexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        assertEquals(MJLexer.CURLY_LBRACKET, mjLexer.nextToken().getType());
    }

    @Test public void TestCurlyRBracket() throws IOException {
        String classString = "}";
        InputStream targetStream = new ByteArrayInputStream(classString.getBytes());
        Lexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        assertEquals(MJLexer.CURLY_RBRACKET, mjLexer.nextToken().getType());
    }

    @Test public void TestIf() throws IOException {
        String classString = "if";
        InputStream targetStream = new ByteArrayInputStream(classString.getBytes());
        Lexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        assertEquals(MJLexer.IF, mjLexer.nextToken().getType());
    }

    @Test public void TestWhile() throws IOException {
        String classString = "while";
        InputStream targetStream = new ByteArrayInputStream(classString.getBytes());
        Lexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        assertEquals(MJLexer.WHILE, mjLexer.nextToken().getType());
    }

    @Test public void TestPrintln() throws IOException {
        String classString = "System.out.println";
        InputStream targetStream = new ByteArrayInputStream(classString.getBytes());
        Lexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        assertEquals(MJLexer.PRINTLN, mjLexer.nextToken().getType());
    }

    @Test public void TestReturn() throws IOException {
        String classString = "return";
        InputStream targetStream = new ByteArrayInputStream(classString.getBytes());
        Lexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        assertEquals(MJLexer.RETURN, mjLexer.nextToken().getType());
    }

    @Test public void TestEquals() throws IOException {
        String classString = "=";
        InputStream targetStream = new ByteArrayInputStream(classString.getBytes());
        Lexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        assertEquals(MJLexer.EQUALS, mjLexer.nextToken().getType());
    }

    @Test public void TestAnd() throws IOException {
        String classString = "&&";
        InputStream targetStream = new ByteArrayInputStream(classString.getBytes());
        Lexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        assertEquals(MJLexer.AND, mjLexer.nextToken().getType());
    }

    @Test public void TestNot() throws IOException {
        String classString = "!";
        InputStream targetStream = new ByteArrayInputStream(classString.getBytes());
        Lexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        assertEquals(MJLexer.NOT, mjLexer.nextToken().getType());
    }

    @Test public void TestLT() throws IOException {
        String classString = "<";
        InputStream targetStream = new ByteArrayInputStream(classString.getBytes());
        Lexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        assertEquals(MJLexer.LT, mjLexer.nextToken().getType());
    }

    @Test public void TestPlus() throws IOException {
        String classString = "+";
        InputStream targetStream = new ByteArrayInputStream(classString.getBytes());
        Lexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        assertEquals(MJLexer.PLUS, mjLexer.nextToken().getType());
    }

    @Test public void TestMinus() throws IOException {
        String classString = "-";
        InputStream targetStream = new ByteArrayInputStream(classString.getBytes());
        Lexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        assertEquals(MJLexer.MINUS, mjLexer.nextToken().getType());
    }

    @Test public void TestMult() throws IOException {
        String classString = "*";
        InputStream targetStream = new ByteArrayInputStream(classString.getBytes());
        Lexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        assertEquals(MJLexer.MULT, mjLexer.nextToken().getType());
    }

    @Test public void TestDot() throws IOException {
        String classString = ".";
        InputStream targetStream = new ByteArrayInputStream(classString.getBytes());
        Lexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        assertEquals(MJLexer.DOT, mjLexer.nextToken().getType());
    }

    @Test public void TestTrue() throws IOException {
        String classString = "true";
        InputStream targetStream = new ByteArrayInputStream(classString.getBytes());
        Lexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        assertEquals(MJLexer.TRUE, mjLexer.nextToken().getType());
    }

    @Test public void TestFalse() throws IOException {
        String classString = "false";
        InputStream targetStream = new ByteArrayInputStream(classString.getBytes());
        Lexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        assertEquals(MJLexer.FALSE, mjLexer.nextToken().getType());
    }

    @Test public void TestThis() throws IOException {
        String classString = "this";
        InputStream targetStream = new ByteArrayInputStream(classString.getBytes());
        Lexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        assertEquals(MJLexer.THIS, mjLexer.nextToken().getType());
    }

    @Test public void TestNew() throws IOException {
        String classString = "new";
        InputStream targetStream = new ByteArrayInputStream(classString.getBytes());
        Lexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        assertEquals(MJLexer.NEW, mjLexer.nextToken().getType());
    }

    @Test public void TestID() throws IOException {
        String classString = "my_string";
        InputStream targetStream = new ByteArrayInputStream(classString.getBytes());
        Lexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        assertEquals(MJLexer.ID, mjLexer.nextToken().getType());
    }

    @Test public void TestIntLiteral() throws IOException {
        String classString = "1234";
        InputStream targetStream = new ByteArrayInputStream(classString.getBytes());
        Lexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        assertEquals(MJLexer.INT_LITERAL, mjLexer.nextToken().getType());
    }

    @Test public void TestColon() throws IOException {
        String classString = ",";
        InputStream targetStream = new ByteArrayInputStream(classString.getBytes());
        Lexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        assertEquals(MJLexer.COMMA, mjLexer.nextToken().getType());
    }

    @Test public void TestSemiColon() throws IOException {
        String classString = ";";
        InputStream targetStream = new ByteArrayInputStream(classString.getBytes());
        Lexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        assertEquals(MJLexer.SEMI_COLON, mjLexer.nextToken().getType());
    }

    @Test public void TestNewLine() throws IOException {
        String classString = "new" + "\n" + "line";
        InputStream targetStream = new ByteArrayInputStream(classString.getBytes());
        Lexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        assertEquals(MJLexer.NEW, mjLexer.nextToken().getType());
        assertEquals(MJLexer.ID, mjLexer.nextToken().getType());
    }

    @Test public void TestComment() throws IOException {
        String classString = "// some comment" + "\n" + "2";
        InputStream targetStream = new ByteArrayInputStream(classString.getBytes());
        Lexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        assertEquals(MJLexer.INT_LITERAL, mjLexer.nextToken().getType());
    }

}
