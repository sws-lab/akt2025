grammar Parm;
@header { package toylangs.parm; }

// Seda reeglit pole vaja muuta
init : expr EOF;

// Seda reeglit tuleb muuta / täiendada
// (Ilmselt soovid ka defineerida uusi abireegleid)
expr
    : 'implementeeri mind!'
    ;

// Neid soovitame jätta nii:
WS: [ \n\r\t]+ -> skip;
