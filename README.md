# MiniJavaCompiler

Compiler for a subset of Java. BNF (Backus-Naur Form) is available on https://www.cambridge.org/resources/052182060X/MCIIJ2e/grammar.htm.

Antlr4 was used for the Lexer construction; a recursive-descent parser was implemented based on the BNF; semantic checks and code generation are implemented by applying the visitor pattern on the AST nodes.
