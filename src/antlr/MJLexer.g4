lexer grammar MJLexer;

CLASS_KW: 'class';
PUBLIC_KW: 'public';
STATIC_KW: 'static';
VOID_KW: 'void';
MAIN_KW: 'main';
STRING_KW: 'String';
EXTENDS_KW: 'extends';
LENGTH_KW: 'length';

INT_KW: 'int';
BOOLEAN_KW: 'boolean';
INT_ARRAY: 'int[]';

LPARENS: '(';
RPARENS: ')';
LBRACKET: '[';
RBRACKET: ']';
CURLY_LBRACKET: '{';
CURLY_RBRACKET: '}';

IF: 'if';
ELSE: 'else';
WHILE: 'while';
PRINTLN: 'System.out.println';
RETURN: 'return';

EQUALS: '=';

AND: '&&';
NOT: '!';
LT: '<';
PLUS: '+';
MINUS: '-';
MULT: '*';

DOT: '.';

TRUE: 'true';
FALSE: 'false';

THIS: 'this';
NEW: 'new';

ID: [a-zA-Z]+[0-9a-zA-Z_]*;
INT_LITERAL: [0-9]+;

COMMA: ',';
SEMI_COLON: ';';

WS: [ \n\r\t]+ -> skip;
COMMENT: '//' (~[\n\r])* -> skip;



