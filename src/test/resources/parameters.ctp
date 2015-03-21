html(content='',attributes=[]) ::= {
  <html@for(var=attributes,do={ @@ivar})>@content</html>
}

testByName() ::= {@html(content="inhalt",attributes=['lang="de"'])}
testBySequence() ::= {@html("content",['lang="en"'])}