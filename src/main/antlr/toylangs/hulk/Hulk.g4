grammar Hulk;
@header { package toylangs.hulk; }

// Seda reeglit pole vaja muuta
init : program EOF;

// Seda reeglit tuleb muuta / täiendada
// (Ilmselt soovid ka defineerida uusi abireegleid)
program
    : 'implementeeri mind!'
    ;
