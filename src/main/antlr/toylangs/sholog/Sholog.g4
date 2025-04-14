grammar Sholog;
@header { package toylangs.sholog; }

// Seda reeglit pole vaja muuta
init : expr EOF;

// Seda reeglit tuleb muuta / täiendada
// (Ilmselt soovid ka defineerida uusi abireegleid)
expr
    : 'implementeeri mind!'
    ;
