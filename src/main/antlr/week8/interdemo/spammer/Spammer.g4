grammar Spammer;
@header { package week8.interdemo.spammer; }

init: line? (NL line?)*;

line: NAME ':' EMAIL  #Email
    | NAME ':' NUMBER #Phone
    ;

fragment Word: [A-Z][a-z]+;
fragment Comp: Word ('-' Word)*;
NAME: Comp (' ' Comp)*;

NUMBER: '+'? ([0-9]+) ([ -] [0-9]+)*;

fragment PART : [a-zA-Z0-9-]+;
EMAIL: PART ('.' PART)* '@' PART ('.' PART)*;

NL: '\r'? '\n';
WS: [ \t]+ -> skip;
