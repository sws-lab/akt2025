grammar Safdi;
@header { package toylangs.safdi; }

// Seda reeglit pole vaja muuta
init : expr EOF;

// Seda reeglit tuleb muuta / täiendada
// (Ilmselt soovid ka defineerida uusi abireegleid)
expr
    : left=expr '+' right=term # Add
    | term                     # SimpleExpr
    ;

// kihiline grammatika, sest muidu on võimatu (?) samaaegselt saavutada:
// 1. * ja / on sama prioriteediga
// 2. * ja / on vasakassotsiatiivsed
// 3. recover ainult / juures
term
    : left=term '*' right=factor    # Mul
    | left=term '/' right=factor
        ('recover' recover=factor)? # Div
    | factor                        # SimpleTerm
    ;

factor
    : Num          # Num
    | Var          # Var
    | '-' factor   # Neg
    | '(' expr ')' # Paren
    ;

Num : '0'|[1-9][0-9]*;
Var : [a-zA-Z]+;

WS : [ \r\n\t] -> skip;
