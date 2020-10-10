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
    : (
    ( mbs=methodBodyStatement
    | lvd=localVarDec
    ) SEMICOLON
    | SEMICOLON
    )*
    ;

localVarDec
    : varModifier* varDec
    ;

varModifier
    : 'final'
    ;

methodBodyStatement
    : methodCall
    ;

methodCall
    : methodName '(' callArguments ')'
    ;

callArguments
    : e1=expression ( ',' e2=expression )*
    ;

expression
    : objectRefName
    | literal
    ;

objectRefName
    : identifier
    | objectRefName '.' identifier
    ;

literal
    : STRING_LITERAL
    | CHAR_LITERAL
    | INTEGER_LITERAL
    ;

methodName
    : identifier ( '.' identifier )*
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

SEMICOLON : ';' ;

STRING_LITERAL : '"' ( '\\"' | . )*? '"' ;
CHAR_LITERAL : '\'' . '\'' ;
INTEGER_LITERAL : DIGIT+ [lL]?;

IDENTIFIER : (AZ | '_' | '$')  (AZ | DIGIT | '_' | '$')* ;

WHITESPACE : (NEW_LINE | ' ' | '\t') -> channel(HIDDEN) ;

fragment NEW_LINE : '\n' | '\r' '\n'? ;

fragment AZ : (LOWERCASE | UPPERCASE) ;
fragment LOWERCASE : [a-z] ;
fragment UPPERCASE : [A-Z] ;

fragment DIGIT : [0-9] ;

ERROR: . ;