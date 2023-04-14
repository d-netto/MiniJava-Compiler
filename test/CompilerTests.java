import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.antlr.v4.runtime.CharStreams;
import org.junit.Test;

import antlr_lexer.MJLexer;
import codegen_simple.SimpleCodegenVisitor;
import parser.MJParser;
import parser.ast.GoalNode;
import semantics.BuilderVisitor;
import semantics.TypesVisitor;

public class CompilerTests {
  // All of the following test files were taken form
  // https://www.cambridge.org/resources/052182060X/

  @Test
  public void TestBinarySearch() throws IOException {
    String simpleProgram =
        new String(Files.readAllBytes(Paths.get("test/test_files/binary_search.mjava")));
    InputStream targetStream = new ByteArrayInputStream(simpleProgram.getBytes());
    MJLexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
    MJParser mjParser = new MJParser(mjLexer);
    GoalNode goal = mjParser.parseGoal();
    BuilderVisitor builderVis = new BuilderVisitor();
    goal.accept(builderVis);
    TypesVisitor typesVis = new TypesVisitor(builderVis);
    goal.accept(typesVis);
    SimpleCodegenVisitor codegenVis = new SimpleCodegenVisitor(typesVis);
    goal.accept(codegenVis);
  }

  @Test
  public void TestBinaryTree() throws IOException {
    String simpleProgram =
        new String(Files.readAllBytes(Paths.get("test/test_files/binary_tree.mjava")));
    InputStream targetStream = new ByteArrayInputStream(simpleProgram.getBytes());
    MJLexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
    MJParser mjParser = new MJParser(mjLexer);
    GoalNode goal = mjParser.parseGoal();
    BuilderVisitor builderVis = new BuilderVisitor();
    goal.accept(builderVis);
    TypesVisitor typesVis = new TypesVisitor(builderVis);
    goal.accept(typesVis);
    SimpleCodegenVisitor codegenVis = new SimpleCodegenVisitor(typesVis);
    goal.accept(codegenVis);
  }

  @Test
  public void TestBubbleSort() throws IOException {
    String simpleProgram =
        new String(Files.readAllBytes(Paths.get("test/test_files/bubble_sort.mjava")));
    InputStream targetStream = new ByteArrayInputStream(simpleProgram.getBytes());
    MJLexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
    MJParser mjParser = new MJParser(mjLexer);
    GoalNode goal = mjParser.parseGoal();
    BuilderVisitor builderVis = new BuilderVisitor();
    goal.accept(builderVis);
    TypesVisitor typesVis = new TypesVisitor(builderVis);
    goal.accept(typesVis);
    SimpleCodegenVisitor codegenVis = new SimpleCodegenVisitor(typesVis);
    goal.accept(codegenVis);
  }

  @Test
  public void TestFactorial() throws IOException {
    String simpleProgram =
        new String(Files.readAllBytes(Paths.get("test/test_files/factorial.mjava")));
    InputStream targetStream = new ByteArrayInputStream(simpleProgram.getBytes());
    MJLexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
    MJParser mjParser = new MJParser(mjLexer);
    GoalNode goal = mjParser.parseGoal();
    BuilderVisitor builderVis = new BuilderVisitor();
    goal.accept(builderVis);
    TypesVisitor typesVis = new TypesVisitor(builderVis);
    goal.accept(typesVis);
    SimpleCodegenVisitor codegenVis = new SimpleCodegenVisitor(typesVis);
    goal.accept(codegenVis);
  }

  @Test
  public void TestLinear() throws IOException {
    String simpleProgram =
        new String(Files.readAllBytes(Paths.get("test/test_files/linear_search.mjava")));
    InputStream targetStream = new ByteArrayInputStream(simpleProgram.getBytes());
    MJLexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
    MJParser mjParser = new MJParser(mjLexer);
    GoalNode goal = mjParser.parseGoal();
    BuilderVisitor builderVis = new BuilderVisitor();
    goal.accept(builderVis);
    TypesVisitor typesVis = new TypesVisitor(builderVis);
    goal.accept(typesVis);
    SimpleCodegenVisitor codegenVis = new SimpleCodegenVisitor(typesVis);
    goal.accept(codegenVis);
  }

  @Test
  public void TestLinkedList() throws IOException {
    String simpleProgram =
        new String(Files.readAllBytes(Paths.get("test/test_files/linked_list.mjava")));
    InputStream targetStream = new ByteArrayInputStream(simpleProgram.getBytes());
    MJLexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
    MJParser mjParser = new MJParser(mjLexer);
    GoalNode goal = mjParser.parseGoal();
    BuilderVisitor builderVis = new BuilderVisitor();
    goal.accept(builderVis);
    TypesVisitor typesVis = new TypesVisitor(builderVis);
    goal.accept(typesVis);
    SimpleCodegenVisitor codegenVis = new SimpleCodegenVisitor(typesVis);
    goal.accept(codegenVis);
  }

  @Test
  public void TestQuickSort() throws IOException {
    String simpleProgram =
        new String(Files.readAllBytes(Paths.get("test/test_files/quick_sort.mjava")));
    InputStream targetStream = new ByteArrayInputStream(simpleProgram.getBytes());
    MJLexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
    MJParser mjParser = new MJParser(mjLexer);
    GoalNode goal = mjParser.parseGoal();
    BuilderVisitor builderVis = new BuilderVisitor();
    goal.accept(builderVis);
    TypesVisitor typesVis = new TypesVisitor(builderVis);
    goal.accept(typesVis);
    SimpleCodegenVisitor codegenVis = new SimpleCodegenVisitor(typesVis);
    goal.accept(codegenVis);
  }

  @Test
  public void TestTreeVisitor() throws IOException {
    String simpleProgram =
        new String(Files.readAllBytes(Paths.get("test/test_files/tree_visitor.mjava")));
    InputStream targetStream = new ByteArrayInputStream(simpleProgram.getBytes());
    MJLexer mjLexer = new MJLexer(CharStreams.fromStream(targetStream));
    MJParser mjParser = new MJParser(mjLexer);
    GoalNode goal = mjParser.parseGoal();
    BuilderVisitor builderVis = new BuilderVisitor();
    goal.accept(builderVis);
    TypesVisitor typesVis = new TypesVisitor(builderVis);
    goal.accept(typesVis);
    SimpleCodegenVisitor codegenVis = new SimpleCodegenVisitor(typesVis);
    goal.accept(codegenVis);
  }
}
