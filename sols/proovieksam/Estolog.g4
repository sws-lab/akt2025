grammar Estolog;
@header { package proovieksam; }

// Seda reeglit pole vaja muuta
init : prog EOF;

// Seda reeglit tuleb muuta / täiendada
// (Ilmselt soovid ka defineerida uusi abireegleid)
prog
    : (def ';')* avaldis
    ;

def
    : Muutuja ':=' avaldis
    ;

avaldis
    : avaldis 'JA' avaldis                              # BinOp
    | avaldis 'VOI' avaldis                             # BinOp
    | avaldis 'NING' avaldis                            # BinOp
    | 'KUI' avaldis 'SIIS' avaldis ('MUIDU' avaldis)?   # KuiSiis
    | lihtneAvaldis '=' lihtneAvaldis                   # BinOp
    | lihtneAvaldis                                     # Lihtne
    ;

lihtneAvaldis
    : Muutuja                                           # Muutuja
    | Arv                                               # Literaal
    | '(' avaldis ')'                                   # Sulud
    ;

Muutuja : [a-zA-Z]+;
Arv : [01];

WS : [ \r\n\t] -> skip;
