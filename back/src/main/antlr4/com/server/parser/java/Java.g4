grammar Java;

taskEOF
    : task EOF
    ;

task
    : classDec
    ;

classDec
    : classHeader '{' classBody '}'
    ;

classHeader
    : classModifier* 'class' identifier
    ;

classBody
    : ( fieldDec SEMICOLON
    | methodDec
    | SEMICOLON
    )*
    ;

fieldDec
    : fieldModifier* varDec
    ;

varDec
    : type identifier ( '=' expression )?
    ;

fieldModifier
    : 'public'
    | 'protected'
    | 'private'
    | 'static'
    | 'final'
    | 'transient'
    | 'volatile'
    ;

methodDec
    : methodHeader '{' methodBody '}'
    ;

methodHeader
    : methodModifier* methodResult identifier methodArgs
    ;

methodArgs
    : '(' ( singleMethodArg ( ',' singleMethodArg )* )? ')'
    ;

singleMethodArg
    : type identifier
    ;

methodBody
    : statementList
    ;

statementList
    : statement*
    ;

statement
    : blockStatement
    | expressionStatement
    | ifElseStatement
    | switchStatement
    | emptyStatement
    ;

switchStatement
    : SWITCH '(' expression ')' '{' switchElement* '}'
    ;

switchElement
    : switchElementLabel+ statementList
    ;

switchElementLabel
    : CASE expression ':'
    | DEFAULT ':'
    ;

blockStatement
    : '{' statementList '}'
    ;

ifElseStatement
    : IF '(' cond=expression ')' statement (ELSE statement)?
    ;

emptyStatement
    : SEMICOLON
    ;

expressionStatement
    : ( call
    | methodVarDec
    | assignment
    ) SEMICOLON
    ;

assignment
    : identifier '=' expression
    ;

methodVarDec
    : varModifier* varDec
    ;

varModifier
    : 'final'
    ;

call
    : callName '(' callArguments? ')'
    ;

callName
    : firstSeg=identifier ( '.' secSeg=identifier )?
    | specialCallName
    ;

specialCallName
    : 'System.out.print'
    | 'System.out.println'
    ;

callArguments
    : expression ( ',' expression )*
    ;

expression
   : unOp=('+' | '-')? exprAtom
   | expression op=('*' | '/' | '%') expression
   | expression op=('+' | '-') expression
   | expression op=('<' | '<=' | '>' | '>=') expression
   | expression eq=('==' | '!=') expression
   | expression andOp='&&' expression
   | expression orOp='||' expression
   ;

exprAtom
    :  LPAREN expression RPAREN
    | call
    | objectRefName
    | literal
    | nullExpr
    ;

nullExpr
    : NULL
    ;

objectRefName
    : identifier
    | objectRefName '.' identifier
    ;

literal
    : STRING_LITERAL
    | CHAR_LITERAL
    | INTEGER_LITERAL
    | FLOAT_LITERAL
    | BOOLEAN_LITERAL
    ;

methodResult
    : 'void'
    | type
    ;

type
    : primitiveType
    | objectType
    | arrayType
    ;

arrayType
    : ( primitiveType | objectType )  '[' ']'
    ;

objectType
    : identifier
    ;

primitiveType
    : 'short'
    | 'int'
    | 'long'
    | 'float'
    | 'double'
    | 'byte'
    | 'char'
    | 'boolean'
    ;

methodModifier
    : 'public'
    | 'protected'
    | 'private'
    | 'abstract'
    | 'static'
    | 'final'
    | 'strictfp'
    | 'synchronized'
    | 'native'
    ;

classModifier
    : 'public'
    | 'protected'
    | 'private'
    | 'abstract'
    | 'static'
    | 'final'
    | 'strictfp'
    ;

identifier
    : IDENTIFIER
    ;

CASE        : 'case' ;
DEFAULT     : 'default' ;
ELSE        : 'else' ;
IF          : 'if' ;
LPAREN      : '(' ;
NULL        : 'null' ;
RPAREN      : ')' ;
SEMICOLON   : ';' ;
SWITCH      : 'switch' ;

STRING_LITERAL : '"' ( '\\"' | . )*? '"' ;
CHAR_LITERAL : '\'' . '\'' ;
INTEGER_LITERAL : DIGIT+ [lL]?;
FLOAT_LITERAL : DIGIT+ '.' DIGIT* [fFdD]? | '.' DIGIT+ [fFdD]? | DIGIT+ [fFdD] ;
BOOLEAN_LITERAL : FALSE | TRUE ;
FALSE: 'false' ;
TRUE: 'true';

IDENTIFIER : (AZ | '_' | '$')  (AZ | DIGIT | '_' | '$')* ;

WHITESPACE : (NEW_LINE | ' ' | '\t') -> channel(HIDDEN) ;
COMMENT : '/*' .*? '*/' -> channel(HIDDEN) ;
LINE_COMMENT : '//' ~[\r\n]* -> channel(HIDDEN) ;

fragment NEW_LINE : '\n' | '\r' '\n'? ;

fragment AZ : (LOWERCASE | UPPERCASE) ;
fragment LOWERCASE : [a-z] ;
fragment UPPERCASE : [A-Z] ;

fragment DIGIT : [0-9] ;

ERROR: . ;