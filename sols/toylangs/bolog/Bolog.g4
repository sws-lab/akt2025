grammar Bolog;
@header { package toylangs.bolog; }

// Seda reeglit pole vaja muuta
init : prog EOF;

// Seda reeglit tuleb muuta / täiendada
// (Ilmselt soovid ka defineerida uusi abireegleid)
prog
    : NL* expr (NL+ expr)* NL*
    ;

expr
    : TvLit                     #TvLit
    | Var                       #Var
    | '(' expr ')'              #Paren
    | '!' expr                  #Unary
    | expr op='&&' expr         #Binary
    | expr op='||' expr         #Binary
    | expr op='<>' expr         #Binary
    | con=expr 'kui' (ass+=expr ((',' ass+=expr)* 'ja' ass+=expr)?)? #Imp
    ;

TvLit : 'JAH' | 'EI';
Var : [A-Z]+;

// Neid soovitame jätta nii:
NL: '\r'? '\n';
WS: [ \t]+ -> skip;
