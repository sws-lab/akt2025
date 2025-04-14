grammar Pullet;
@header { package toylangs.pullet; }

// Seda reeglit pole vaja muuta
init : expr EOF;

// Seda reeglit tuleb muuta / täiendada
// (Ilmselt soovid ka defineerida uusi abireegleid)
expr
    : 'implementeeri mind!'
    ;

WS : [ \n\r\t] -> skip;
