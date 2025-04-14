grammar Hulk;
@header { package toylangs.hulk; }

// Seda reeglit pole vaja muuta
init : program EOF;

// Seda reeglit tuleb muuta / täiendada
// (Ilmselt soovid ka defineerida uusi abireegleid)
program
    : stmt (NL stmt)*
    ;

stmt
    : Set op=':=' expr ('|' cond)?
    | Set op=('<-' | '->') elements ('|' cond)?
    ;

cond
    : expr    'subset' expr             #Subset
    | Element 'in' expr                 #Incl
    ;

expr
    : expr op=('+' | '&' | '-') expr    #BinOp
    | Set                               #Var
    | '{' elements? '}'                 #Lit
    | '(' expr ')'                      #Paren
    ;

elements
    : Element (',' Element)*
    ;

Set : [A-Z];
Element : [a-z];

NL : ('\r')?'\n';
WS : [ \t] -> skip;
