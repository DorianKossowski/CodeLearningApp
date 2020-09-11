grammar JavaTask;

rulesEOF
    : rules EOF
    ;

rules
    : singleRule+
    ;

singleRule
    : ' '
    ;

STRING_LITERAL : '"' ( '\\"' | . )*? '"' ;

WHITESPACE : (NEW_LINE | ' ' | '\t') -> channel(HIDDEN) ;
fragment NEW_LINE : '\n' | '\r' '\n'? ;

ERROR: . ;