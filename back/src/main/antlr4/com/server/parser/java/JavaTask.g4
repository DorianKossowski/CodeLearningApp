grammar JavaTask;

rulesEOF
    : rules EOF
    ;

rules
    : singleRule*
    ;

singleRule
    : ARROW ( methodRule | statementRule )
    ;

statementRule
    : 'statement' statementRuleSpec+
    ;

statementRuleSpec
    : ( statementMethodRuleSpec | statementTextRuleSpec )
    ;

statementTextRuleSpec
    : 'with' 'text' ':' STRING_LITERAL
    ;

statementMethodRuleSpec
    : 'in' 'method' ':' valueOrEmpty
    ;

methodRule
    : 'method' methodRuleSpec+
    ;

methodRuleSpec
    : 'with' ( methodNameRuleSpec | methodArgsRuleSpec | methodModifiersRuleSpec | methodResultRuleSpec )
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

STRING_LITERAL : '"' ( '\\"' | . )*? '"' ;

WHITESPACE : (NEW_LINE | ' ' | '\t') -> channel(HIDDEN) ;
fragment NEW_LINE : '\n' | '\r' '\n'? ;

ERROR: . ;