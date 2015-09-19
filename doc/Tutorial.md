#Tutorial

To demonstrate the facilities of ComTemplate this tutorial demonstrates creating a web site generator step by step.

##Setting Up the Basics 

To generate a website we first need a library of templates we could generate. For the first we will provide
- html scaffold tags (html, head, body)
- h1
- p

###Starting with the Generator

Our generator should be very simple. The command line would look like this: 

`java HtmlGenerator templateName [argument1 [argument2[argument3 ...]]]`

So we start writing the generator into a file `HtmlGenerator.java`:

```Java
public class HtmlGenerator {

	public static void main(String[] args) throws IOException {
		String name = args[0];
		Object[] arguments = Stream.of(args).skip(1).toArray(len -> new Object[len]);
		
		TemplateLoader loader = new CurrentPathTemplateLoader();

		TemplateDefinition template = loader.loadDefinition(name);;
		
		System.out.println(template.evaluateNative(arguments));
	}

}
```

Now we need a template to generate. So let's start.
 

###Starting with the html-Tag

We start with creating a file `html.ctp` containing:

```
html(content) ::= {
  <html>
    `content`
  </html>
}
```

Call `java HtmlGenerator html.html myContent` and you get

```
  <html>
    myContent
  </html>
```

###Adding Auto Indentation

You probably note that the template code is indented by two chars. Common html files do not indent the top level element. So let's find a way to skip the initial indentation. Change `html.ctp` to

```
html(content) ::= {
  <html>
    `content`
  </html>
}.indent()
```

Call `java HtmlGenerator html.html myContent` and you get

```
<html>
  myContent
</html>
```

Auto indentation asserts that each called template (even anonymous ones) is indented by the number given as argument. Individual indentations in called templates are discarded.

###Writing more tags

To have a full html document we shall need also a `head` and `body` tag. These would look like this:

```
head(content) ::= {
  <head>
    `content`
  </head>
}

body(content) ::= {
  <body>
    `content`
  </body>
}
```

Call `java HtmlGenerator html.head myContent` and you get

```
<head>
  myContent
</head>
```

Analogous with `java HtmlGenerator html.body myContent`

###Generating a 'Hello World' html file

Now that a minimal template set is provided we can start and write a 'Hello World' in `helloworld.ctp` example:
 
```
helloworld() ::= {
  `html({
    `head()`
    `body("Hello World")`
  })`
}.indent()
```

Call `java HtmlGenerator helloworld.helloworld` and you get

```
<html>
  <head>
    
  </head>
  <body>
    Hello World
  </body>
</html>
```

## Working with ComTemplate

Now that we have a working example we can continue to do some more interesting tasks.

###Dealing with errors

The HTMLGenerator does yet not handle errors. To verify this change `helloworld.ctp` to:
 
```
hello world() ::= {
  `html({
    `head()`
    `body("Hello World")`
  })`
}.indent()
```

A message will be displayed followed by some cryptic stack trace. To display the message without the stacktrace we change `HtmlGenerator.java`:
 
```Java
public class HtmlGenerator {

	public static void main(String[] args) throws IOException {
		String name = args[0];
		Object[] arguments = Stream.of(args).skip(1).toArray(len -> new Object[len]);
		
		TemplateLoader loader = new CurrentPathTemplateLoader();

		try {
			TemplateDefinition template = loader.loadDefinition(name);
			
			System.out.println(template.evaluateNative(arguments));
		} catch (ComtemplateException e) {
			System.out.println(e.getMessage());
		}
	}

}
```

Now the stack trace is gone and the message is displayed.

###Adding Headlines and Paragraphs

Our minimal html file should contains a headline and a paragraph, so let's define

```
h1(content="") ::= {
  <h1>`content`</h1>
}

p(content="") ::= {
  <p>`content`</p>
}
```

and edit `helloworld.ctp` and create a new template helloworld2:

```
helloworld2() ::= {
  `html({
    `head()`
    `body({
      `h1("Hello World")`
      `p("My first static Template")`
     })`
  })`
}.indent()
```

Call `java HtmlGenerator helloworld.helloworld` and you get

```
<html>
  <head>
    
  </head>
  <body>
    <h1>Hello World</h1>
    <p>My first static Template</p>
  </body>
</html>
```

###Adding HTML Attributes

Yet the markup for `p` and `h1` is very short and do not support important features of html: html attributes. We start with adding html attribute support:

```
h1(content="", attributes=[]) ::= {
  <h1`for(att=attributes,do={ `@att`})`>`content`</h1>
}

p(content="", attributes=[]) ::= {
  <p`for(att=attributes,do={ `@att`})`>`content`</p>
}
```

then we change `helloworld.ctp`:

```
helloworld2() ::= {
  `html({
    `head()`
    `body({
      `h1(content="Hello World",attributes=["id=\"hello\"", "class=\"headline\""])`
      `p(content="My first static Template",attributes=["class=\"paragraph\""])`
     })`
  })`
}.indent()
```

Call `java HtmlGenerator helloworld.helloworld2` and you get

```
<html>
  <head>
    
  </head>
  <body>
    <h1>Hello World</h1>
    <p>My first static Template</p>
  </body>
</html>
```

### Refactoring Attributes

As you can see the arguments of the template call of `h1` and `p1` make the example quite long. One also can get easily confused with the double quotes. So we start a refactoring. This is based on the idea that attributes like `id` or `class` are commonly used in html, so we could extract them to their own templates in `attributes.ctp`:
 
```
id(value="") ::= {id="`value`"}

class(value="") ::= {class="`value`"}
```

then we change `helloworld.ctp`:

```
helloworld2() ::= {
  `html({
    `head()`
    `body({
      `h1(content="Hello World",attributes=[id("hello"), class("headline")])`
      `p(content="My first static Template",attributes=[class("paragraph")])`
     })`
  })`
}.indent()
```
 
 ## Advanced Topics
 
 ### Messages in different Languages
 
Maybe you want to generate similar html files for different languages. Using a different set of messages. For this purpose we first rewrite our main template:  

```
helloworld3(lang="en") ::= {
  `html({
    `head()`
    `body({
      `h1(content=msg.(lang).HELLO_WORLD,attributes=[id("hello"), class("headline")])`
      `p(content=msg.(lang).MY_FIRST_TEMPLATE,attributes=[class("paragraph")])`
     })`
  })`
}.indent()
``` 

This would yet not compile, but we can add a message import:

````