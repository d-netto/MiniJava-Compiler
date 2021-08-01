package parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.antlr.v4.runtime.CharStreams;
import org.junit.Test;

import antlr.MJLexer;

public class MJParserTest {

    // TODO: write unit tests --> try to parse "smaller nodes", not whole GoalNode

    @Test public void TestSimpleProgram() throws IOException {
        String simpleProgram = "class Main {\n" + "public static void main(String[] args) {\n"
                + "System.out.println(123);\n" + "}" + "}";
        InputStream targetStream = new ByteArrayInputStream(simpleProgram.getBytes());
        MJLexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        MJParser mjParser = new MJParser(mjLexer);
        mjParser.parseGoal();
    }

    @Test public void TestOneNonMainClass() throws IOException {
        String simpleProgram = "class Main {\n" + "public static void main(String[] args) {\n"
                + "System.out.println(4);\n" + "}\n" + "}\n" + "\n" + "class Foo {\n" + "int bar;\n"
                + "public int baz() {\n" + "return 2;\n" + "}\n" + "}\n";
        InputStream targetStream = new ByteArrayInputStream(simpleProgram.getBytes());
        MJLexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        MJParser mjParser = new MJParser(mjLexer);
        mjParser.parseGoal();
    }

}
