grammar Comtemplate;

options {
  superClass=ComtemplateBaseParser;
}

@lexer::members {
    public static final int WHITESPACE = 1;
    public static final int COMMENTS = 2;
}

templateFile 
	: imports clauses EOF
	;
	
imports
	: importctp*
	;

importctp
	: Import qualifiedName #importrule
	| Import qualifiedWildcard #importpackage
	;

qualifiedWildcard
	: qualifiedName '.' Wildcard
	;

qualifiedName
	: Identifier ('.' Identifier)*
	;
	
clauses
	: clause*
	;

clause
	: valueDefinition # valueClause
	| functionDefinition # functionClause
	;

valueDefinition
	: name=Identifier '::=' value 
	;

functionDefinition
	: name=Identifier '(' parameters? ')' '::=' template  # templateDefinition
	| name=Identifier '(' parameters? ')' '::=' value # objectDefinition // value that is not a template
	;

template
	:  '{' templateBody '}'
	;

templateBody
	: {enable(ComtemplateLexer.WHITESPACE);} templateChunk* {disable(ComtemplateLexer.WHITESPACE);}  
	;

templateChunk
	: {disable(ComtemplateLexer.WHITESPACE);}templateReference{enable(ComtemplateLexer.WHITESPACE);}# codeChunk
	| inlineText # textChunk
	| Whitespace+ #textWhitespace
	;

templateReference
	: ReferenceMark value ReferenceMark
	;

parameters
	: parameter (',' parameter)*
	;

parameter
	: name=Identifier (':' type=Identifier)? ('=' value)?
	;

ref
	: name=Identifier '(' attributes ')' #refTemplateByName
	| name=Identifier '(' items ')' #refTemplateBySequence
	| name=Identifier '(' ')' #refTemplateEmpty
	| name=Identifier '(' (item | attribute) (',' (item | attribute))+ ')'? {syntaxError();} #refTemplateError
	| EscapeMark? name=Identifier  #refVariable
	;

map 
	: '[' attributes? ']'
	;

function
	: name=Identifier '(' items? ')'
	;

attributes
	: attribute (',' attribute)*
	;

attribute
	: name=Identifier '=' value
	;

list 
	: '[' items? ']'
	;

items
	: item (',' item)*
	;

item
	: value
	;

value
	: template # valueAnonymousTemplate
	| list # valueList
	| map # valueMap
	| scalar # valueScalar
	| ref # valueRef
	| value '.' function # valueFunction
	| value '.' attr=Identifier # valueAttribute
	| value ('~' value)+ # valueConcat
	| value '?' # valueExists
	| value '?:' value # valueDefault
	;

scalar
	: IntegerLiteral # intScalar
	| DecimalLiteral # decScalar
	| StringLiteral # strScalar
	| BooleanLiteral # boolScalar
	;

inlineText
	: inlineToken+
	;

inlineToken
	: Import
	| Define
	| '[' | ']'
	| '(' | ')'
	| '='
	| ':' | '.' | ';' | ',' | '?' | '!' | '~'
	| IntegerLiteral
	| DecimalLiteral
	| StringLiteral
	| BooleanLiteral
	| Identifier
	| Wildcard
	| '\\'
	| '\\' ('{' |  '}' | '.' | '?' | ':' | '~' | '(' | ')')
	| EscapedComment
	| AnythingElse
	;

Define
	: '::='
	;
	
Import
	: 'import'
	;

EscapeMark
	: '@'
	;

ReferenceMark
	: '`'
	;

IntegerLiteral
	: '-'? DEC_LITERAL
	;

DecimalLiteral
    :   '-'? DEC_LITERAL '.' [0-9]+
	;

StringLiteral
    :  '\'' ( ESCAPE_SEQUENCE | . )*? '\''
    |  '"' ( ESCAPE_SEQUENCE | . )*? '"'
    ;

BooleanLiteral
    : 'true'
    | 'false'
    ;

Identifier
    : LETTER (LETTER|DIGIT)*
    ;

Wildcard
    : '*'
    ;

Whitespace
	: [ \t\n\r] -> channel(WHITESPACE)
	;

Comment
    : '#' ~[\n\r]* -> channel(COMMENTS) 
    ;

EscapedComment
    : ESCAPED_COMMENT
    ;

AnythingElse
	: .
	;

fragment DEC_LITERAL
	: ('0' | [1-9] [0-9]*)
	;

fragment ESCAPED_COMMENT
    :   '\\' '#'
    ;

fragment ESCAPE_SEQUENCE
    :   '\\' [btnfr\"\'\\]
    |   UNICODE_ESCAPE
    ;

fragment UNICODE_ESCAPE
    :   '\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
    ;

fragment LETTER
	: [a-zA-Z]
	;

fragment DIGIT
	: [0-9]
	;

fragment HEX_DIGIT
	: [0-9a-fA-F]
	;