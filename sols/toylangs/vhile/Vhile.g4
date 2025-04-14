grammar Vhile;
@header { package toylangs.vhile; }

// Seda reeglit pole vaja muuta
init : stmt EOF;

// Seda reeglit tuleb muuta / täiendada
// (Ilmselt soovid ka defineerida uusi abireegleid)
stmt
    : Var '=' compare                     # Assign
    | '{' (stmt (';' stmt)*)? '}'         # Block
    | 'while' '(' compare ')' stmt        # While
    | 'escape' (':' Num)?                 # Escape
    ;

compare
    : left=expr op=('=='|'!=') right=expr # BinaryCompare
    | expr                                # SimpleCompare
    ;

expr
    : Num                                 # Num
    | Var                                 # Var
    | '(' compare ')'                     # Paren
    | left=expr op='*' right=expr         # BinaryExpr
    | left=expr op='+' right=expr         # BinaryExpr
    ;

Num : '0' | '-'? [1-9][0-9]*;
Var : [a-z]+;

WS : [ \r\n\t] -> skip;
