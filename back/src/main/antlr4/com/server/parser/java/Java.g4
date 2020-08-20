grammar Java;

exerciseEOF
    : exercise EOF
    ;

exercise
    : classDec
    ;

classDec
    : classModifier* 'class' identifier '{' .*? '}'
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