grammar JavaTask;

rulesEOF
    : rules EOF
    ;

rules
    : singleRule*
    ;

singleRule
    : ARROW methodRule
    ;

methodRule
    : 'method' methodRuleSpec+
    ;

methodRuleSpec
    : 'with' ( methodNameRuleSpec
    | methodArgsRuleSpec
    )
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