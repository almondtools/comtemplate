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
	: Import (local='.')? qualifiedName #importrule
	| Import (local='.')? qualifiedWildcard #importpackage
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
	: name=Identifier '(' parameters? ')' '::=' value # templateDefinition
	| EscapeMark name=Identifier '(' parameters? ')' '::=' value # objectDefinition
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
	: ReferenceOpen value ReferenceClose
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
	| name=Identifier '(' items ',' attributes ')' #refTemplateByMixed
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
	| value '.' '(' ref ')' #valueVirtual
	| value ('~' value)+ # valueConcat
	| value '?' # valueExists
	| value '?:' value # valueDefault
	;

scalar
	: IntegerLiteral # intScalar
	| DecimalLiteral # decScalar
	| stringLiteral # strScalar
	| BooleanLiteral # boolScalar
	;

stringLiteral
	: '"' {enable(ComtemplateLexer.WHITESPACE);} doubleQuoteToken* {disable(ComtemplateLexer.WHITESPACE);}'"'
	| '\'' {enable(ComtemplateLexer.WHITESPACE);} singleQuoteToken* {disable(ComtemplateLexer.WHITESPACE);}'\''
	;

doubleQuoteToken
	: Import
	| Define
	| ReferenceOpen | ReferenceClose
	| '<' | '>'
	| '[' | ']'
	| '(' | ')'
	| '\''
	| '='
	| ':' | '.' | ';' | ',' | '?' | '!' | '~'
	| IntegerLiteral
	| DecimalLiteral
	| BooleanLiteral
	| Identifier
	| Wildcard
	| '\\'
	| '\\' ('{' |  '}' | '.' | '?' | ':' | '~' | '(' | ')' | '"' | '@')
	| EscapedComment
	| AnythingElse
	| Whitespace
	;

singleQuoteToken
	: Import
	| Define
	| ReferenceOpen | ReferenceClose
	| '<' | '>'
	| '[' | ']'
	| '(' | ')'
	| '"'
	| '='
	| ':' | '.' | ';' | ',' | '?' | '!' | '~'
	| IntegerLiteral
	| DecimalLiteral
	| BooleanLiteral
	| Identifier
	| Wildcard
	| '\\'
	| '\\' ('{' |  '}' | '.' | '?' | ':' | '~' | '(' | ')' | '\'' | '@')
	| EscapedComment
	| AnythingElse
	| Whitespace
	;

inlineText
	: inlineToken+
	;

inlineToken
	: Import
	| Define
	| '<' | '>'
	| '[' | ']'
	| '(' | ')'
	| '"' | '\''
	| '='
	| ':' | '.' | ';' | ',' | '?' | '!' | '~'
	| IntegerLiteral
	| DecimalLiteral
	| BooleanLiteral
	| Identifier
	| Wildcard
	| '\\'
	| '\\' ('{' |  '}' | '.' | '?' | ':' | '~' | '(' | ')')
	| EscapedReferenceMark
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

ReferenceOpen
	: '<<'
	;

ReferenceClose
	: '>>'
	;

EscapedReferenceMark
	: '\\<'
	| '\\>'
	;

IntegerLiteral
	: '-'? DEC_LITERAL
	;

DecimalLiteral
    :   '-'? DEC_LITERAL '.' [0-9]+
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
	: [ \t\n\r] -> channel(1)
	;

Comment
    : '#' ~[\n\r]* -> channel(2) 
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
	: [_a-zA-Z]
	;

fragment DIGIT
	: [0-9]
	;

fragment HEX_DIGIT
	: [0-9a-fA-F]
	;