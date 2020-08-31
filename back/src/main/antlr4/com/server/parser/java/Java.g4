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
    { $classAst = new ClassAst($classHeader.header, $classBody.body); }
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

methodName returns [String name]
@init{ List<String> ids = new ArrayList<>(); }
    : identifier { ids.add($identifier.text); } ( '.' identifier { ids.add($identifier.text); } )*
    { $name = JavaGrammarHelper.createMethodName(ids); }
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