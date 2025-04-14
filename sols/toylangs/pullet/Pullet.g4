grammar Pullet;
@header { package toylangs.pullet; }

// Seda reeglit pole vaja muuta
init : expr EOF;

// Seda reeglit tuleb muuta / täiendada
// (Ilmselt soovid ka defineerida uusi abireegleid)
expr
    : Num                                                             #Num
    | Var                                                             #Var
    | '(' expr ')'                                                    #Paren
    | expr '-' expr                                                   #Diff
    | 'let' Var '=' expr ( ';' Var '=' expr )* 'in' body=expr         #Binding
    | 'sum' Var '=' lo+=expr 'to' hi+=expr
      ( ';' Var '=' lo+=expr 'to' hi+=expr )* 'in' body=expr          #Sum
    ;

Var : [a-zA-Z]+;
Num : [0-9]+;

WS : [ \n\r\t] -> skip;
