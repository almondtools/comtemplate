# ComTemplate

ComTemplate is a component based static content generator and template engine. ComTemplate does not aim to win the battle of best separation of view and model, although it is more strict than most template engines for Java.

Its primary focus is the generation of static resources from parameterized template files. This simplifies the problem of separating view and model: The model is constant and could be inlined into the pages. ComTemplate supports both:
- physically separated model (in Java) and view (in ComTemplate DSL)
- integrated model and view (both in ComTemplate DSL)

To keep the code clean and testible ComTemplate DSL is designed to be an extensible **pure functional template language**: 
- no mutable variables
- no side effects
- users can extend the language by custom templates (args -> string) and functions (args -> any data type) 

This brings some advantages: 
- Testing gets quite simple: Because every template is a pure function tests can be specified as input-output-pairs
- Debugging gets easy: Because no side effects can occur, each value has exactly one definition position, a false value should be easily tracked down
- Separating model and view gets easy, mixing them up gets hard.

## Starting with ComTemplate

You may start with a simple "Hello World"-Template, written in a file `hello.ctp`

    hello(who) ::= {
      Hello <<who>>!
    }

To evaluate this template in java write a simple main program:

     public class Hello {
       public static void main(String[] args) throws IOException {
	    	TemplateGroup group = new TemplateGroupBuilder("hello", "hello.ctp").build();
       	TemplateDefinition definition = group.getDefinition("hello");
       	System.out.println(definition.evaluateNative("World")); //prints Hello World
       }
     }

More advanced examples will be part of the [Tutorial](doc/Tutorial.md).

## [ComTemplate Tutorial](doc/Tutorial.md)

## [ComTemplate Syntax](doc/TemplateFileSyntax.md)

## [Template Library](doc/TemplateLibrary.md)

## [Advanced Topics](doc/AdvancedTopics.md)

## Planned Features

 - main templates (implicit rule signature, imports at any position of the code)
 - better error reporting
 - reflective references to variables (experimental)
 - deploy in a maven repository 

