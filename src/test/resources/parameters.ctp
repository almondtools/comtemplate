html(content='',attributes=[]) ::= {
  <html`for(att=attributes,do={ `@att`})`>`content`</html>
}

testByName() ::= {`html(content="inhalt",attributes=['lang="de"'])`}
testBySequence() ::= {`html("content",['lang="en"'])`}