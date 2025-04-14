grammar Bolog;
@header { package toylangs.bolog; }

// Seda reeglit pole vaja muuta
init : prog EOF;

// Seda reeglit tuleb muuta / täiendada
// (Ilmselt soovid ka defineerida uusi abireegleid)
prog
    : 'implementeeri mind!'
    ;

// Neid soovitame jätta nii:
NL: '\r'? '\n';
WS: [ \t]+ -> skip;
