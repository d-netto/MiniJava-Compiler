lexer grammar MJLexer;

CLASS_KW: 'class';
PUBLIC_KW: 'public';
STATIC_KW: 'static';
VOID_KW: 'void';
MAIN_KW: 'main';
STRING_KW: 'String';
EXTENDS_KW: 'extends';

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
WHILE: 'while';
PRINTLN: 'System.out.println';
RETURN: 'return';

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

COMMA: ',';
SEMI_COLON: ';';

WS: [ \t\r\n]+ -> skip;
COMMENT: '//' [~WS]* -> skip;



