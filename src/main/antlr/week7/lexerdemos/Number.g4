grammar Number;
@header { package week7.lexerdemos; }

number: INT;
fragment DIGIT : '0'..'9';
INT   :  DIGIT+;
