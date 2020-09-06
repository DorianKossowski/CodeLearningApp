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
@init{ List<Method> methods = new ArrayList<>(); List<Variable> fields = new ArrayList<>(); }
    : ( methodDec { methods.add($methodDec.method); }
    | fieldDec { fields.add($fieldDec.v); }
    )*
    { $body = new ClassBody(methods, fields); }
    ;

fieldDec returns [Variable v]
    : fieldModifier* type varDec ';'
    { $v = new Variable($type.text, $varDec.id, $varDec.e); }
    ;

varDec returns [String id, Expression e]
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

methodDec returns [Method method]
    : methodHeader '{' methodBody '}'
    { $method = new Method($methodHeader.header, $methodBody.body); }
    ;

methodHeader returns [MethodHeader header]
    : methodModifier* methodResult identifier methodArgs
    ;

methodArgs
    : '(' ( singleMethodArg ( ',' singleMethodArg )* )? ')'
    ;

singleMethodArg
    : type identifier
    ;

methodBody returns [MethodBody body]
@init{ List<Statement> sList = new ArrayList<>(); }
    : ( mbs=methodBodyStatement { sList.add($mbs.s); }
    | lvd=localVarDec { sList.add($lvd.v); }
    | ';'
    )*
    { $body = new MethodBody(sList); }
    ;

localVarDec returns [Variable v]
    : varModifier* type varDec ';'
    ;

varModifier
    : 'final'
    ;

methodBodyStatement returns [Statement s]
    : methodCall { $s = $methodCall.mc; }
    ;

methodCall returns [MethodCall mc]
    : methodName '(' callArguments ')' ';'
    { $mc = new MethodCall($methodName.name, $callArguments.args); }
    ;

callArguments returns [List<Expression> args = new ArrayList<>()]
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

methodName returns [String name]
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