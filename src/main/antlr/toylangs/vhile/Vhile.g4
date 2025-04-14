grammar Vhile;
@header { package toylangs.vhile; }

// Seda reeglit pole vaja muuta
init : stmt EOF;

// Seda reeglit tuleb muuta / täiendada
// (Ilmselt soovid ka defineerida uusi abireegleid)
stmt
    : 'implementeeri mind!'
    ;
