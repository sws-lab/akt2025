grammar GreedyLexer;
@header { package week7.lexerdemos; }

init: testLexer | testParser;

testLexer: (Kolm|Neli)*;
testParser: (kolm|neli)*;

Kolm: 'a' 'a' 'a';
Neli: 'a' 'a' 'a' 'a';

kolm: 'b' 'b' 'b';
neli: 'b' 'b' 'b' 'b';

WS: [ \t\r\n]+ -> skip;

// proovi sisendeid:
// aaaaaa
// bbbbbb
// aaaaaaa
// bbbbbbb
