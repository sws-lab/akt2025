grammar Imp;
@header { package toylangs.imp; }

// Seda reeglit pole vaja muuta
init : prog EOF;

// Seda reeglit tuleb muuta / täiendada
// (Ilmselt soovid ka defineerida uusi abireegleid)
prog : (assign ',')* expr;

assign : Ident '=' expr;

expr
    : Int            #Num
    | Ident          #Var
    | '(' expr ')'   #Paren
    | '-' expr       #Neg
    | expr '/' expr  #Div
    | expr '+' expr  #Add
    ;

Ident: [a-zA-Z];
Int: [0-9]|[1-9][0-9]+;

WS: [ \t\r\n]+ -> skip;
