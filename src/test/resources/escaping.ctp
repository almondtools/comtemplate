notescaped ::= "notescaped"

escapedExample1() ::= {
<\<escaped>\>
}

escapedExample2() ::= {
\<<escaped\>>
}

escapedHtml() ::= {
<\<<html>myhtml</html>\>>
}

notescapedExample() ::= {
\<<<notescaped()>>>
}