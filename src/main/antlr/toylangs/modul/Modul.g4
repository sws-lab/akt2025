grammar Modul;
@header { package toylangs.modul; }

// Seda reeglit pole vaja muuta
init : prog EOF;

// Seda reeglit tuleb muuta / täiendada
// (Ilmselt soovid ka defineerida uusi abireegleid)
prog
    : 'implementeeri mind!'
    ;
