grammar Sholog;
@header { package toylangs.sholog; }

// Seda reeglit pole vaja muuta
init : expr EOF;

// Seda reeglit tuleb muuta / täiendada
// (Ilmselt soovid ka defineerida uusi abireegleid)
expr
    : Lit                       # Lit
    | Var                       # Var
    | Err                       # Err
    | '(' expr ')'              # Paren
    | '~' expr                  # Not
    | expr op=('/\\'|'&&') expr # Op
    | expr op='+' expr          # Op
    | expr op=('\\/'|'||') expr # Op
    ;

Lit : [TF];
Var : [a-z]+;
Err : 'E' [0-9]+; // lekserireegel, et mitte lubada tühikut E ja arvu vahel

WS : [ \r\n\t] -> skip;
