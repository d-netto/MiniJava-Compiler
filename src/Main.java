import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.antlr.v4.runtime.CharStreams;

import antlr_lexer.MJLexer;
import codegen_simple.SimpleCodegenVisitor;
import parser.MJParser;
import parser.ast.GoalNode;
import semantics.BuilderVisitor;
import semantics.TypesVisitor;

public class Main {

  public static void main(String args[]) throws IOException {
    String program = new String(Files.readAllBytes(Paths.get(args[0])));
    InputStream targetStream = new ByteArrayInputStream(program.getBytes());
    MJLexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
    MJParser mjParser = new MJParser(mjLexer);
    GoalNode goal = mjParser.parseGoal();
    BuilderVisitor builderVis = new BuilderVisitor();
    goal.accept(builderVis);
    TypesVisitor typesVis = new TypesVisitor(builderVis);
    goal.accept(typesVis);
    SimpleCodegenVisitor codegenVis = new SimpleCodegenVisitor(typesVis);
    goal.accept(codegenVis);
    String assemblyStr = codegenVis.getDataRegion() + codegenVis.getTextRegion() + "\n";
    byte[] strToBytes = assemblyStr.getBytes();
    Files.write(Paths.get(args[1]), strToBytes);
  }
}
