grammar JavaTask;

rulesEOF
    : rules EOF
    ;

rules
    : singleRule+
    ;

singleRule
    : ARROW methodRule
    ;

methodRule
    : 'method' methodRuleSpec+
    ;

methodRuleSpec
    : 'with' 'name' ':' STRING_LITERAL
    ;

ARROW : '-' '>' ;

STRING_LITERAL : '"' ( '\\"' | . )*? '"' ;

WHITESPACE : (NEW_LINE | ' ' | '\t') -> channel(HIDDEN) ;
fragment NEW_LINE : '\n' | '\r' '\n'? ;

ERROR: . ;