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
    | constructorDec
    | methodDec
    | SEMICOLON
    )*
    ;

constructorDec
    : constructorHeader '{' methodBody '}'
    ;

constructorHeader
    : constructorModifier? identifier methodArgs
    ;

constructorModifier
	: 'public'
	| 'protected'
	| 'private'
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
    | expressionStatementSemicolon
    | ifElseStatement
    | switchStatement
    | forStatement
    | whileStatement
    | doWhileStatement
    | emptyStatement
    ;

doWhileStatement
    : DO statement WHILE '(' expression ')' SEMICOLON
    ;

whileStatement
    : WHILE '(' expression ')' statement
    ;

forStatement
    : FOR
    '(' initExpr=expressionStatement? ';' condExpr=expression? ';' updateExpr=expressionStatement? ')'
    statement
    ;

expressionStatementSemicolon
    : expressionStatement SEMICOLON
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
    : call
    | methodVarDec
    | assignment
    | breakStatement
    | returnStatement
    ;

returnStatement
	: RETURN expression?
	;

breakStatement
    : BREAK
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

BREAK       : 'break' ;
CASE        : 'case' ;
DEFAULT     : 'default' ;
DO          : 'do' ;
ELSE        : 'else' ;
FOR         : 'for' ;
IF          : 'if' ;
LPAREN      : '(' ;
NULL        : 'null' ;
RETURN      : 'return' ;
RPAREN      : ')' ;
SEMICOLON   : ';' ;
SWITCH      : 'switch' ;
WHILE       : 'while' ;

STRING_LITERAL : '"' ( '\\"' | . )*? '"' ;
CHAR_LITERAL : '\'' . '\'' ;
INTEGER_LITERAL : DIGIT+ [lL]?;
FLOAT_LITERAL : DIGIT+ '.' DIGIT* [fFdD]? | '.' DIGIT+ [fFdD]? | DIGIT+ [fFdD] ;
BOOLEAN_LITERAL : FALSE | TRUE ;
FALSE: 'false' ;
TRUE: 'true';

IDENTIFIER : (AZ | '_' | '$')  (AZ | DIGIT | '_' | '$')* ;

WHITESPACE : (NEW_LINE | ' ' | '\t' | '\u200B' | 'u000C') -> channel(HIDDEN) ;
COMMENT : '/*' .*? '*/' -> channel(HIDDEN) ;
LINE_COMMENT : '//' ~[\r\n]* -> channel(HIDDEN) ;

fragment NEW_LINE : '\n' | '\r' '\n'? ;

fragment AZ : (LOWERCASE | UPPERCASE) ;
fragment LOWERCASE : [a-z] ;
fragment UPPERCASE : [A-Z] ;

fragment DIGIT : [0-9] ;

ERROR: . ;