grammar Dialoog;
@header { package toylangs.dialoog; }

// Ära seda reeglit muuda, selle kaudu testitakse grammatikat
init : prog EOF;

// Seda reeglit tuleb muuta / täiendada
// (Ilmselt soovid ka defineerida uusi abireegleid)
prog
    : decl* 'Arvuta:' expr
    ;


decl : Var 'on' typ=(BType|IntType) '!' ;

expr
    : TvLit                     #TvLit
    | IntLit                    #IntLit
    | Var                       #Var
    | 'Oota' expr 'Valmis'      #Paren
    | op=('!'|'-') expr         #Unary
    | expr op=('*'|'/') expr    #Binary
    | expr op=('+'|'-') expr    #Binary
    | expr op='on' expr         #Binary
    | expr op='&' expr          #Binary
    | expr op='|' expr          #Binary
    | 'Kas' expr '?'
        'Jah:' expr
        'Ei:' expr
      'Selge'                   #Ifte
    ;

IntLit : '0'|[1-9][0-9]*;
TvLit : 'jah' | 'ei';

BType : 'bool' [a-z]*;
IntType : 'int' [a-z]*;
Var : [a-z]+;

// Siin soovitame tühjust ignoreerida:
WS : [ \r\n\t] -> skip;
