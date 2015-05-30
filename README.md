# ComTemplate

ComTemplate provides a template engine for composing text templates. 

ComTemplate provides a **pure functional template language**:

 - There are no mutable variables and no side effects (except those hidden in the host language)
 - This makes mixing up layout and code very hard 

## Starting with ComTemplate

ComTemplate is yet not sufficiently documented. But it is sufficiently tested - each feature has a corresponding unit test.

If something does not work properly, open an issue. If you have patches for code or documentation (even typos) then open a pull request.

## [ComTemplate Syntax](doc/TemplateFileSyntax.md)

## [Template Library](doc/TemplateLibrary.md)

## [Advanced Topics](doc/AdvancedTopics.md)

## Planned Features

 - adhoc templates (files with implicit rule & inline imports)
 - better error reporting
 - reflective references to variables (experimental)
 - deploy in a maven repository 

