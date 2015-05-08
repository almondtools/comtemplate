snippet() ::= {
`if(cond=@context?,then=@context,else="nothing")`
}

defaultsnippet() ::= {
`context?:"nothing"`
}

withContext(context="someContext") ::= {`snippet()`}

withoutContext() ::= {`snippet()`}

defaultWithContext(context="someContext") ::= {`snippet()`}

defaultWithoutContext() ::= {`snippet()`}