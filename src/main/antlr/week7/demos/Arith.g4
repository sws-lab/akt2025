grammar Arith; // grammatika nimi peab klappima failinimega
@header { package week7.demos; } // Seda on vaja, et loodud failid läheks õige paketti.

// parseri reeglid algavad väikeste tähtedega
expr
    :   expr '+' term
    |   expr '-' term
    |   term
    ;

term
    :   term '*' factor
    |   term '/' factor
    |   factor
    ;

factor
    :   '(' expr ')'
    |   Variable
    |   Literal
    ;

// lekseri reeglid algavad suurte tähtedega
Variable
    :   [a-zA-Z_][a-zA-Z_0-9]*
    ;

Literal
    :   [0-9]|[1-9][0-9]+
    ;

// Järgmise lekseemi suuname ära (-> skip).
WS: [ \t\r\n]+ -> skip;