grammar Java;

@header {
import com.server.parser.java.ast.*;
}

exerciseEOF
    : exercise EOF
    ;

exercise
    : classDec
    ;

classDec
    : classHeader '{' classBody '}'
    ;

classHeader returns [ClassHeader header]
    : classModifier* 'class' identifier
    { $header = new ClassHeader($identifier.text); }
    ;

classBody returns [ClassBody body]
@init{ List<Method> methods = new ArrayList<>(); }
    : ( methodDec { methods.add($methodDec.method); } )*
    { $body = new ClassBody(methods); }
    ;

methodDec returns [Method method]
    : methodHeader '{' methodBody '}'
    { $method = new Method($methodHeader.header); }
    ;

methodHeader returns [MethodHeader header]
    : methodModifier* methodResult identifier methodArgs
    { $header = new MethodHeader($methodResult.text, $identifier.text, $methodArgs.args); }
    ;

methodArgs returns [List<Variable> args = new ArrayList<>()]
    : '(' ( s=singleMethodArg { $args.add($s.var); } ( ',' s=singleMethodArg { $args.add($s.var); } )* )? ')'
    ;

singleMethodArg returns [Variable var]
    : type identifier
    { $var = new Variable($type.text, $identifier.text); }
    ;

methodBody
    : .*?
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

IDENTIFIER : (AZ | '_' | '$')  (AZ | DIGIT | '_' | '$')* ;

WHITESPACE : (NEW_LINE | ' ' | '\t') -> channel(HIDDEN) ;

fragment NEW_LINE : '\n' | '\r' '\n'? ;

fragment AZ : (LOWERCASE | UPPERCASE) ;
fragment LOWERCASE : [a-z] ;
fragment UPPERCASE : [A-Z] ;

fragment DIGIT : [0-9] ;