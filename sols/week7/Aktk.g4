grammar Aktk;
@header { package week7; }

program
    : statements EOF
    ;

statements
    : statement (';' statement)*
    ;

statement
    : ifStatement
    | whileStatement
    | assignStatement
    | variableDeclaration
    | expression
    | functionDefinition
    | returnStatement
    | blockStatement
    ;

blockStatement
    : '{' statements '}'
    ;

ifStatement
    : 'if' expression 'then' thenStatement=statement 'else' elseStatement=statement
    ;

whileStatement
    : 'while' expression 'do' statement
    ;

returnStatement
    : 'return' expression
    ;

assignStatement
    : Identifier '=' expression
    ;

variableDeclaration
    : 'var' VariableName=Identifier (':' VariableType=Identifier)? ('=' expression)?
    ;

functionDefinition
    : 'fun' FunctionName=Identifier
      '(' (ParameterName+=Identifier ':' ParameterType+=Identifier (',' ParameterName+=Identifier ':' ParameterType+=Identifier)*)? ')' ('->' ReturnType=Identifier)?
      blockStatement
    ;

expression
    : compareExpression
    ;

compareExpression
    : sumExpression ('>'|'<'|'>='|'<='|'=='|'!=') sumExpression # BinaryCompare
    | sumExpression                                             # SimpleCompare
    ;

sumExpression
    : sumExpression ('+'|'-') termExpression # BinarySum
    | termExpression                         # SimpleSum
    ;

termExpression
    : termExpression ('*'|'/'|'%') factorExpression # BinaryTerm
    | factorExpression                              # SimpleTerm
    ;

factorExpression
    : '-' factorExpression # UnaryMinus
    | callExpression       # SimpleFactor
    ;

callExpression
    : Identifier '(' (expression (',' expression)*)? ')' # FunctionCall
    | basicExpression                                    # SimpleCall
    ;

basicExpression
    : Identifier         # Variable
    | Integer            # IntegerLiteral
    | String             # StringLiteral
    | '(' expression ')' # Parenthesis
    ;

Identifier
    : [a-zA-Z][a-zA-Z0-9_]*
    ;

Integer
    : ('0'|[1-9][0-9]*)
    ;

String
    : '"' ~["\n\r]* '"' // Tildega saab väljendada eitust.
    ;                   // Siin ~["\n\r] tähistab suvalist tähte
                        // mis pole jutumärk ega reavahetuse sümbol.

Comment
    : '/*' .*? '*/' -> skip
    ;

Whitespace
    : [ \t\r\n]+ -> skip
    ;
