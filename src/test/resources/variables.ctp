snippet() ::= {
<<if(cond=@context?,then=@context,else="nothing")>>
}

defaultsnippet() ::= {
<<@context?:"nothing">>
}

ignoringerrors() ::= {
<<@context!.next!.next?:"nothing">>
}

withContext(context="someContext") ::= {<<snippet()>>}

withoutContext() ::= {<<snippet()>>}

defaultWithContext(context="someContext") ::= {<<defaultsnippet()>>}

defaultWithoutContext() ::= {<<defaultsnippet()>>}

ignoringErrorsWithSufficientContext(context=[next=[next="next"]]) ::= {<<ignoringerrors()>>}

ignoringErrorsWithAlmostSufficientContext(context=[next="next"]) ::= {<<ignoringerrors()>>}

ignoringErrorsWithContext(context=[]) ::= {<<ignoringerrors()>>}

ignoringErrorsWithoutContext() ::= {<<ignoringerrors()>>}