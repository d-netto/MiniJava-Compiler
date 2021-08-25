package codegen;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.antlr.v4.runtime.CharStreams;
import org.junit.Test;

import antlr_lexer.MJLexer;
import codegen_simple.SimpleCodegenVisitor;
import parser.MJParser;
import parser.ast.GoalNode;
import semantics.BuilderVisitor;
import semantics.TypesVisitor;

public class CodegenTest {

    @Test public void TestClassOtherThanMain() throws IOException {
        String simpleProgram = "class Main {\n" + "public static void main(String[] args) {\n"
                + "System.out.println(4);\n" + "}\n" + "}\n" + "\n" + "class Foo {\n" + "int bar;\n"
                + "public int baz() {\n" + "return 2;\n" + "}\n" + "}\n";
        InputStream targetStream = new ByteArrayInputStream(simpleProgram.getBytes());
        MJLexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
        MJParser mjParser = new MJParser(mjLexer);
        GoalNode goal = mjParser.parseGoal();
        BuilderVisitor builderVis = new BuilderVisitor();
        goal.accept(builderVis);
        TypesVisitor typesVis = new TypesVisitor(builderVis);
        goal.accept(typesVis);
        SimpleCodegenVisitor codegenVis = new SimpleCodegenVisitor(builderVis);
        goal.accept(codegenVis);
        System.out.println(codegenVis.getPreamble() + "\n" + codegenVis.getFunctionsRegion());
    }

}
