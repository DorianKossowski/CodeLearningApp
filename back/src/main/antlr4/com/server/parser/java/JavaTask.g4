grammar JavaTask;

rulesEOF
    : rules EOF
    ;

rules
    : singleRule*
    ;

singleRule
    : ARROW ( classRule | methodRule | statementRule | variableRule )
    ;

classRule
    : 'class' classRuleSpec+
    ;

classRuleSpec
    : WITH ( classNameSpec | classConstructorSpec | classFieldSpec )
    | logInfo
    ;

classFieldSpec
    : 'field' fieldRuleSpec+
    ;

fieldRuleSpec
    : WITH ( modifiersRuleSpec | typeRuleSpec | fieldNameRuleSpec | valueRuleSpec | logInfo )
    ;

valueRuleSpec
    : 'value' ':' STRING_LITERAL
    ;

fieldNameRuleSpec
    : 'name' ':' STRING_LITERAL
    ;

typeRuleSpec
    : 'type' ':' STRING_LITERAL
    ;

classConstructorSpec
    : 'constructor' constructorRuleSpec+
    ;

constructorRuleSpec
    : WITH ( constructorNameRuleSpec | methodArgsRuleSpec )
    ;

constructorNameRuleSpec
    : 'name' ':' STRING_LITERAL
    ;

classNameSpec
    : 'name' ':' STRING_LITERAL
    ;

variableRule
    : 'variable' variableRuleSpec+
    ;

variableRuleSpec
    : WITH textRuleSpec
    | logInfo
    ;

statementRule
    : 'statement' statementRuleSpec+
    ;

statementRuleSpec
    : statementMethodRuleSpec
    | WITH ( textRuleSpec | statementResolvedRuleSpec | ifSpec | elseIfSpec | switchExpr | switchLabel | forIteration
       | whileIteration | doWhileIteration)
    | elseSpec
    | logInfo
    ;

doWhileIteration
    : 'do' 'while' 'iteration' ':' STRING_LITERAL
    ;

whileIteration
    : 'while' 'iteration' ':' STRING_LITERAL
    ;

forIteration
    : 'for' 'iteration' ':' STRING_LITERAL
    ;

switchLabel
    : 'switch' 'label' ':' STRING_LITERAL
    ;

switchExpr
    : 'switch' 'expression' ':' STRING_LITERAL
    ;

elseIfSpec
    : 'else if' ':' STRING_LITERAL
    ;

elseSpec
    : 'is in else'
    ;

ifSpec
    : 'if' ':' STRING_LITERAL
    ;

logInfo
    : 'log' 'info' ':' STRING_LITERAL
    ;

statementResolvedRuleSpec
    : 'resolved' ':' STRING_LITERAL
    ;

textRuleSpec
    : 'text' ':' STRING_LITERAL
    ;

statementMethodRuleSpec
    : 'in' 'method' ':' valueOrEmpty
    ;

methodRule
    : 'method' methodRuleSpec+
    ;

methodRuleSpec
    : WITH ( methodNameRuleSpec | methodArgsRuleSpec | modifiersRuleSpec | methodResultRuleSpec )
    ;

methodResultRuleSpec
    : 'result' ':' STRING_LITERAL
    ;

modifiersRuleSpec
    : 'modifiers' ':' '{' STRING_LITERAL ( ',' STRING_LITERAL )* '}'
    ;

methodArgsRuleSpec
    : 'args' ':' argsElements
    ;

methodNameRuleSpec
    : 'name' ':' valueOrEmpty
    ;

argsElements
    : ( argsElement ( ',' argsElement )* )?
    ;

argsElement
    : '{' valueOrEmpty ',' valueOrEmpty '}'
    ;

valueOrEmpty
    : STRING_LITERAL | EMPTY
    ;

ARROW : '-' '>' ;
EMPTY : '-' ;
WITH : 'with' ;

STRING_LITERAL : '"' ( '\\"' | . )*? '"' ;

WHITESPACE : (NEW_LINE | ' ' | '\t') -> channel(HIDDEN) ;
fragment NEW_LINE : '\n' | '\r' '\n'? ;

ERROR: . ;