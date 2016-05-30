html(content="") ::= {
  <html>
    <<content>>
  </html>
}

head(content="") ::= {
  <head>
    <<content>>
  </head>
}

body(content="") ::= {
  <body>
    <<content>>
  </body>
}

h1(content="", attributes=[]) ::= {
  <h1<<for(att=attributes,do={ <<@att>>})>>><<content>></h1>
}

p(content="", attributes=[]) ::= {
  <p<<for(att=attributes,do={ <<@att>>})>>><<content>></p>
}
