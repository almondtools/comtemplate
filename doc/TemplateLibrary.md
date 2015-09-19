#Template Library

In this section one can find an overview over the standard templates, functions and attributes provided by ComTemplate. At first we shall point out which operation can provide which functionality:

 - **templates** allow you to apply a special functional control flow, e.g. conditional evaluation, iterative evaluation, reflective evalutation
 - **attributes** allow you to access properties of the base object. A property is any information the base object can provide without additional information (attributes can be viewed as functions without arguments)
 - **functions** allow you to apply special transformations on a base object. Contrary to arguments, functions allow to process arguments and can therefore not only extract or decorate information but also generate completely new objects.

Most operations of the template library are specific, i.e. they

 - match exactly one name
 - extract a defined signature of arguments
 - have a defined evaluation behavior
 
Yet some operations of the template library are generic, i.e. they

 - can match all names
 - have flexible allowed signatures of arguments
 - are dynamically (dependent on name and signature) mapped to individual behaviors

The following overview will first discuss the templates and specific attributes/functions. Following to these sections there is an overview of generic strategies to evaluate attribute/function on base objects.

## Templates

A template description in the following sections is structured as:

 - a headline containing the name and the purpose of the template
 - a signature with arguments used by the template
 - a description text

### if - Conditional Evaluation

### for - Collection Iteration

### apply - Reflective Evaluation

## Attributes

An attribute description in the following sections is structured as:

 - a headline containing the attribute name and the type of the base object one can apply the attribute to
 - a description text

### size [list, map]

### first [list]

### rest [list]

### last [list]

### trunc [list]

### strip [list]

### keys [map]

### values [map]

### entries [map]

### compress [string, text, evaluated, native object]

### trim [string, text, evaluated, native object]

### indent [text, evaluated]

### empty [string, text native object]

### not [boolean]

## Functions

A function description in the following sections is structured as:

 - a headline containing the function name and signature as well as the type of the base object one can apply the function to
 - a more detailed description of the signature
 - a description text

### item(int) [list]

### separated(string) [list]

## Generic Attributes and Functions

Generic Attributes and Functions are resolved by `DynamicResolver`s. Yet there does not exist a special interface. The feature they have in common is that they can try to match any attribute or function. 

### Beans - Accessing Properties of Java Beans

### Maps - Accessing values of Maps



