import html.*
import attributes.*
import messages.*

helloworld() ::= {
  `html({
    `head()`
    `body("Hello World")`
  })`
}.indent()

helloworld2() ::= {
  `html({
    `head()`
    `body({
      `h1(content="Hello World",attributes=[id("hello"), class("headline")])`
      `p(content="My first static Template",attributes=[class("paragraph")])`
     })`
  })`
}.indent()

helloworld3(lang="en") ::= {
  `html({
    `head()`
    `body({
      `h1(content=msg.(lang).HELLO_WORLD,attributes=[id("hello"), class("headline")])`
      `p(content=msg.(lang).MY_FIRST_TEMPLATE,attributes=[class("paragraph")])`
     })`
  })`
}.indent()

@msg() ::= [
  en=[
  	HELLO_WORLD="Hello World",
  	MY_FIRST_TEMPLATE="My first static Template"
  ],
  de=[
  	HELLO_WORLD="Hallo Welt",
  	MY_FIRST_TEMPLATE="Mein erstes statisches Template"
  ]
]
