# ComTemplate

ComTemplate provides a template engine for composing text templates. 

It utilizes an extensible **pure functional template language**: 
- no mutable variables
- no side effects (except those hidden in custom extensions)
- users can extend the language by custom templates (args -> string) and functions (args -> any data type) 

Advantages:
 - Mixing up layout and code very hard
 - Template code can be easily tested (input of each test is the template arguments, output is the rendered template) 
 

## Starting with ComTemplate

ComTemplate is yet not sufficiently documented. But it is sufficiently tested - each feature has a corresponding unit test.

If something does not work properly, open an issue. If you have patches for code or documentation (even typos) then open a pull request.

## [ComTemplate Syntax](doc/TemplateFileSyntax.md)

## [Template Library](doc/TemplateLibrary.md)

## [Advanced Topics](doc/AdvancedTopics.md)

## Planned Features

 - main templates (implicit rule signature, imports at any position of the code)
 - better error reporting
 - reflective references to variables (experimental)
 - deploy in a maven repository 

