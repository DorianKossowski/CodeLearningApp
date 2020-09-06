grammar Java;

@header {
import com.server.parser.java.ast.*;
import com.server.parser.java.JavaGrammarHelper;
}

exerciseEOF returns [Exercise e]
    : exercise EOF
    { $e = $exercise.e; }
    ;

exercise returns [Exercise e]
    : classDec
    { $e = new Exercise($classDec.classAst); }
    ;

classDec returns [ClassAst classAst]
    : classHeader '{' classBody '}'
    ;

classHeader
    : classModifier* 'class' identifier
    ;

classBody
    : ( fieldDec
    | methodDec
    )*
    ;

fieldDec
    : fieldModifier* type varDec ';'
    ;

varDec
    : identifier ('=' expression )?
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
    : ( mbs=methodBodyStatement
    | lvd=localVarDec
    | ';'
    )*
    ;

localVarDec
    : varModifier* type varDec ';'
    ;

varModifier
    : 'final'
    ;

methodBodyStatement
    : methodCall
    ;

methodCall
    : methodName '(' callArguments ')' ';'
    ;

callArguments
    : e1=expression ( ',' e2=expression )*
    ;

expression
    : literal
    ;

literal
    : stringLiteral
    ;

stringLiteral
    : STRING_LITERAL
    | CHAR_LITERAL
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
    : classModifier
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

STRING_LITERAL : '"' ( '\\"' | . )*? '"' ;
CHAR_LITERAL : '\'' . '\'' ;

IDENTIFIER : (AZ | '_' | '$')  (AZ | DIGIT | '_' | '$')* ;

WHITESPACE : (NEW_LINE | ' ' | '\t') -> channel(HIDDEN) ;

fragment NEW_LINE : '\n' | '\r' '\n'? ;

fragment AZ : (LOWERCASE | UPPERCASE) ;
fragment LOWERCASE : [a-z] ;
fragment UPPERCASE : [A-Z] ;

fragment DIGIT : [0-9] ;

ERROR: . ;