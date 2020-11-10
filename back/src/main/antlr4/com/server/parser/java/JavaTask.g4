grammar JavaTask;

rulesEOF
    : rules EOF
    ;

rules
    : singleRule*
    ;

singleRule
    : ARROW ( methodRule | statementRule | variableRule )
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
    | WITH ( textRuleSpec | statementResolvedRuleSpec | ifSpec | elseIfSpec )
    | elseSpec
    | logInfo
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
    : WITH ( methodNameRuleSpec | methodArgsRuleSpec | methodModifiersRuleSpec | methodResultRuleSpec )
    ;

methodResultRuleSpec
    : 'result' ':' STRING_LITERAL
    ;

methodModifiersRuleSpec
    : 'modifiers' ':' '{' STRING_LITERAL ( ',' STRING_LITERAL )* '}'
    ;

methodArgsRuleSpec
    : 'args' ':' argsElement ( ',' argsElement )*
    ;

methodNameRuleSpec
    : 'name' ':' valueOrEmpty
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