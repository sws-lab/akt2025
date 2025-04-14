grammar Munch;
@header { package week7.lexerdemos; }

init: (t1 | t2 | t3 | t4)*;

// ainult selleks, et ANTLR näitaks, mis reegel valiti:
t1: L1;
t2: L2;
t3: L3;
t4: L4;

L1: 'a' 'ba'*;
L2: 'b' 'ab'*;
L3: 'abd';
L4: 'd'+;

// proovi sisendeid:
// babadd
// ababdddd
// dabbab
// abddb
// babdda
