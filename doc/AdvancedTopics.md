# Advanced Topics

## Global Variables

Any variable defined in the root template call can be considered to be global, i.e. any context variable evaluation can get access to its value.

Sometimes we want to access variables that were not passed to the root template, but are part of the rendering model. For that purpose one can register global variables on `GlobalTemplates` object which is passed to the `TemplateInterpreter`:

    GlobalTemplates templates = GlobalTemplates.defaultTemplates();
    
    templates.register("myModelObject", new NativeObject(myModelObject));
    
    TemplateInterpreter interpreter = new TemplateInterpreter(registry, templates, errorHandler);
    
This interpreter can also resolve global context variables:

     <<@myModelObject.property>>
   

## Advanced Templates

The default way of templating will be the writing of a custom template in a ComTemplate file. Most requirements will be solved with this method.

A more advanced way to create templates is available to java developers. An advanced template can be created as following

 - create a new template class as sub class of `TemplateDefinition`
 - the interpreter uses a set of `GlobalTemplates`, register your template on this object by calling `GlobalTemplates.register(TemplateDefinition)`
 
Use this Method to specify template behavior with a new control flow, that is not yet provided in ComTemplate. Although it is possible to introduce behaviors with side effects with this method, I recommend not to break the **pure functional nature** of ComTemplate, with such an advanced template.

Some standard templates are yet implemented in this way, e.g. `if` (IfTemplate), `for` (ForTemplate) or `apply` (ApplyTemplate).

## Custom Attribute/Function Resolvers

As ComTemplate allows you to define your own templates, it also allows you to define new functions or attributes. Functions and attributes are not managed in the object `GlobalTemplates` but in an object of `ResolverRegistry`.

To define a new attribute/function resolver

 - create a new resolver class as sub class of `Resolver`
 - the interpreter uses a `ResolverRegistry`, register your resolver on this object by calling `Resolver.register(Class<? extends TemplateImmediateExpression>, Resolver)`
 
 Use this method to add new functional expressions to ComTemplate. Although it is possible to introduce behaviors with side effects with this method, I recommend not to break the **pure functional nature** of ComTemplate, with such a resolver.

All standard resolvers yet are implemented in this way.
 

## Inheritance with ComTemplate objects

ComTemplate supports object inheritance. An object is defined as following:

    @Object(name, type) ::= [
      name=name,
      type=type
    ]

To inherit name and type from object, you can define another object:

    @InheritedObject(name, type) ::= Object(name, type)

One can also restrict inherited objects:

    @InheritedObject(name, type) ::= Object(name, "inherited")

Or extend inherited objects with new attributes:

    @InheritedObject(name, type, description) ::= Object(name, type)~[description:description]

ComTemplate even supports multiple Inheritance:

    @WithDescription(description) ::= [
      description=description
    ]
    
    @MultipleInheritedObject(name, type, description) ::= Object(name, type)~WithDescription(description)

## Dealing with dependent sections

Sections in templates usually are independent, i.e the content of one section does not depend on the content of another section.

However sometimes we need to pass information from one section to another, e.g.

 - in HTML we can have content panes that should be triggered by associated menu entries (the menu id and the pane id have to be the same)
 - in Programming languages we may want to use a function that is not yet imported, consequentially an import must be inserted into the import section

As already pointed out ComTemplate is pure functional. So it will not support passing information sideways (as side effects).

For such problems ComTemplate provides a pure functional solution utilizing objects, e.g the HTML example:

    @Menu(items=[]) ::= [
      items=items
    ]
    
    @MenuItem(id, label, content) ::= [
      id=id,
      label=label,
      content=content
    ]

    render(menu) ::= {
      <<renderMenu(menu.items)>>
      <<renderPanes(menu.items)>>
    }
    
    renderMenu(items) ::= {
      <<for(items,{<<@item.label>> triggers <<@item.id>>})>>
    }
    
    renderPanes(items) ::= {
      <<for(items,{<<@item.id>> contains <<@item.content>>})>>
    }

## ComTemplate Reflection

ComTemplate will support applying templates reflectively, i.e. at writing time the template to be called is a variable, the target of this call is evaluated at rendering time. As common with reflective features: Use it only, if your problem needs it.

A typical reflective call would look like the following:

    mytemplate(content) ::= {
      <<content>>
    }
    
    applyTemplate() ::= {
      <<apply(name="mytemplate",arguments=[content='mycontent'])>>
    }
    
## Testing ComTemplate

String template is strongly but dynamic typed. That means that many type problems cannot be detected at compile time, yet the occur at runtime. For small template sets this should not be worth mentioning, but if you decide to maintain a library of templates we recommend to have automatic test suites to test your templates.

Tests can be done in two ways:

 - write your tests in java, some examples are found in `com.almondtools.comtemplate.parser.files` (with template files in `src/test/resources`)
 - use CTPUnit  
