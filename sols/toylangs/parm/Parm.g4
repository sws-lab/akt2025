grammar Parm;
@header { package toylangs.parm; }

// Seda reeglit pole vaja muuta
init : expr EOF;

// Seda reeglit tuleb muuta / täiendada
// (Ilmselt soovid ka defineerida uusi abireegleid)
expr
    : Lit                       #Lit
    | Var                       #Var
    | '(' expr ')'              #Paren
    | expr op='+' expr          #BinOp
    | Var '<-' expr             #Assign
    | expr op=(';'|'|') expr    #BinOp
    ;

Lit : '-'?[0-9]+;
Var : [A-Z][a-z]*;

// Neid soovitame jätta nii:
WS: [ \n\r\t]+ -> skip;
