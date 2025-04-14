grammar Modul;
@header { package toylangs.modul; }

// Seda reeglit pole vaja muuta
init : prog EOF;

// Seda reeglit tuleb muuta / täiendada
// (Ilmselt soovid ka defineerida uusi abireegleid)
prog
    : expr '(' 'mod ' modulus=Num ')'
    ;

expr
    : Num                               # Num
    | Var                               # Var
    | '(' expr ')'                      # Paren
    | base=expr '^' power=Num           # Pow
    | '-' expr                          # Neg
    | left=expr op='*' right=expr       # BinOp
    | left=expr op=('+'|'-') right=expr # BinOp
    ;

Num : '0'|[1-9][0-9]*;
Var : [a-z]+ '_'*;

WS : [ \r\n\t] -> skip;
