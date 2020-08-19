grammar Java;

exerciseEOF
    : exercise EOF
    ;

exercise
    : classDec
    ;

classDec
    : 'class' IDENTIFIER '{' .*? '}'
    ;

IDENTIFIER : (LOWERCASE | UPPERCASE | '_')+ ;

WHITESPACE : (NEW_LINE | ' ' | '\t') -> channel(HIDDEN) ;

fragment NEW_LINE : '\n' | '\r' '\n'? ;
fragment LOWERCASE : [a-z] ;
fragment UPPERCASE : [A-Z] ;