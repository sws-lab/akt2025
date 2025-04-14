grammar Kala;
@header { package week7.demos; }

// Ära seda reeglit ümber nimeta, selle kaudu testitakse grammatikat
init : list EOF;

list : '(' elements? ')';

elements : element (',' element)*;

element
    : Variable   # Variable
    | Null       # Null
    | list       # ListElement
    ;

Null : 'null';

Variable : [a-z]+;

WS : [ \t] -> skip;
