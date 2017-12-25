# Template File

A template file (usually with file extension .ctp) is composed of two main sections 

    ```
    <templateFile> = <imports> <definitions>
    ```

# Imports

Definitions from other template libraries/files can be imported in two ways:

 - import all definitions from another resource:

    ```
    <importAll> = import <packageName>.*
    ```
    
 - import one specific definition from another resource:

    ```
    <importOne> = import <packageName>.<definitionName>
    ```

A package name (identifiers separated by `.`) maps to a specific resource. The packages are resolved by default from the java class loader.
Yet a package defines a file not a directory:

 - `import lib.test.module.*` maps to all definitions of `lib/test/module.ctp` (looked up in the class path)
 - `import lib.test.module.def` maps to the definition of `def` in `lib/test/module.ctp` (looked up in the class path)

# Definitions

The definitions section allows to define:

 - values
 - objects
 - templates

As explained comtemplate is functional so each definition can be viewed as function.

 - objects are functions with arguments that return a structure (map, list, literal)
 - templates are functions with arguments that return a rendered string

## Value Definition

Value definitions define a function without parameters (or simply constants). They follow the pattern

    ```
    <valueName> ::= <expression>
    ```

`valueName` is an identifier,  
`expression` could be any expression (even a template or functional expression)

For example a value `pi` could be defined like this:

		pi ::= 3.14

## Object Definition
Object definitions define a function with parameters returning an expression (not a template expression). They follow the pattern

    ```
    @<valueName>(<paramList>?) ::= <expression>
    ```

`valueName` is an Identifier,  
`paramList` may contain a list of params (which is an Identifier optionally followed by a default value),  
`expression` could be any expression except a templates (actually a map would be the most feasible)

For example a `tableItem` could be defined as followed:

		@tableItem(header,style) ::= [header=header, style=style]

## Template Definition
Template definitions define a function with parameters returning a text. They follow the pattern

    ```
    <valueName>(<paramList>?) ::= <value>
    ```

`valueName` is an Identifier,  
`paramList` may contain a list of params (which is an Identifier optionally followed by a default value),  
`value` should contain the template value

For example a template `add` could look like this:

		add(summand1, summand2) ::= {<<summand1>> + <<summand2>>}

# Expressions and Templates

Basic items in a template file are the templates and the expressions. Expressions represent a functional value:
 
 - a literal or constant
 - a template
 - a builtin function applied to an expression 
 - a function (object definition or template definition) applied to an expression

One can see that a template is a special expression.

In the following text we sometimes refer to the term **ground expression**. A ground expression is the result of an expression evalation and may be one of the following

 - a literal (numeric, boolean, string)
 - a text
 - a map of ground expressions
 - a list of ground expressions

A ground expression does not contain any function or template application.

## Templates

Templates contain the template text enclosed in braces, following the pattern

    ```
    { <templateText> }
    ```

Template text allows following elements:

 - raw text (sequence of characters, some have to be escaped)
 - expression evaluations, these are limited with double brackets, like `` <<expression>> `` where expression is an arbitrary expression

## Local Variable References

A local variable reference evaluates an argument or constant. Such a reference can access all defined arguments of the defining template/object and all global constants. Local variables cannot access definitions in the dynamic application context. Variables or constants can evaluate to text or to other ground expressions. These are defined by their names:

 - In the template definition of `add`
  - `summand1`,`summand2` represent the evaluated arguments of the same name
  - `pi` will be evaluated from the defined constants

## Context Variable Reference

A context variable reference evaluates variables that are defined in the template application context. While local variable references are restricted to template arguments of the defining template and constants, context variables references may climb up the stack of template applications and access variables defined in caller templates. Context variable references evaluate to text or to other ground expressions.

To access variables on the template application context a context escaping prefix (`@`) is required. 

 - `@contextVariable` will be first looked up in the current template, but if not resolved continued on parent scopes

## Integers

A sequence of digits with optional sign is treated as integer literal (leading zeros before a non-zero are not allowed):

 - `0`,`-2`,`42` are integers 
 - `02`,`2.0` are not integers

Integer literals evaluate to integer literals (with the same value) and can be implicitly converted to string/text.

## Decimals

A sequence of digits separated by one `.` with optional sign is treated as decimal literal (leading zeros before a non-zero are not allowed):

 - `2.0`, `-0.038`, `4711.0815` are decimals
 - `-2`, `00.4` are not decimals

Decimal literals evaluate to decimal literals (with the same value) and can be implicitly converted to string/text.

## Booleans

One of the constants `true`or `false` is a boolean literal.

Boolean literals evaluate to boolean literals (with the same value) and can be implicitly converted to string/text.

## Strings

Strings are characters between single (`'`) or double (`"`) quotes. Some characters have to be escaped:

 - `"double quoted"`, `"single quoted"`, `"with \\ escapes"`, `'with \'escapes\''` are strings

String literals evaluate to string literals (with the same value) and can be implicitly converted to text.

## Lists

A list is a sequence of values separated by comma (`,`) and eclosed by brackets (`[`,`]`):

 - `[a, b]`, `["a", 2]` are lists

A list evaluates to a list of ground expressions.

## Maps

A map is a sequence of assignments separated by comma (`,`) and eclosed by brackets (`[`,`]`):

 - `[a="a", b='b']`, `[a=1, b=2]` are maps

A map evaluates to a map of ground expressions.

## Template application

A template application evaluates a template definition with given arguments, following the pattern

    <templateName>(<argumentList|attributeList>) 

The result of template evaluation is always a text. There are two options how to pass arguments:

 - as attribute (named arguments) list, e.g. `add(summand1=1,summand2=2)`
 - as argument list, e.g. `add(1,2)`

Arguments of an attribute list these are unified with the parameter signature. Parameters that are not matched evaluate to their default value or remain unresolved.

Arguments of an argument list are assigned to the parameters in their sequence. Later parameters that are not matched evaluate to their default value or remain unresolved. 
  
## Function Application

A function application evaluates a builtin function on a base expression, following the pattern

    ```
    <baseExpression>.<functionName>(<argumentList>)
    ```

Function applications do not support attribute lists (named arguments).

 - `[1,2].separated(' ')` is a function evaluation (on a list) => `'1 2'`
 - `{ some text }.trim()` is a function evaluation (on an anonymous template) => `{some text}`

Functions can evaluate to any ground expression.

## Attributes

Some data structures (maps, native objects) allow attribute access, written as 
    
    ```
    <baseExpression>.<attributeName>`
    ```

Yet attributes are handled similar to functions, with the only difference that arguments are not allowed:

 - `[a=1,b=2].b` is an attribute evaluation => `2`

Attributes can evaluate to any ground expression.

### List/Map Concatenation

Strings could be concatenated by placing them in sequence. Concatenating lists or maps can be achieved with the concat operator (`~`):

 - `["a","b"]~["c"]` is a list concatenation => `["a","b","c"]` 

Concatenation evaluates to a list or map ground expression (two maps resolve to a map, any other combination to a list).

### Existence Checks

In some cases (context) variable references cannot be resolved in a template. The template can handle these cases either by checking for existence (with `?`) or by setting a default value (with `?:`)

 - `` <<@variable?>> `` is an existence check => `true` if `variable` can be resolved from the context, else `false`
 - `` <<@variable?:'default'>> `` is a default value definition => the value of `variable` or `'default'` if it cannot be resolved

Existence checks resolve to a Boolean Literal, default value definitions resolve to the base expression value or the default value.