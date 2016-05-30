ifThenElse() ::= {
<<if(['1','2'].empty(), then={should not be rendered}, else={cond was <<@cond>>})>>
}

ifThen() ::= {
<<if([].empty(), then={cond was <<@cond>>})>>
}

ifWithAny() ::= {
<<if(any([true,false]), then={any(true,false)=<<@cond>>})>>
}

ifWithNotAny() ::= {
<<if(any([false,false]), then={}, else={any(false,false)=<<@cond>>})>>
}

ifWithAll() ::= {
<<if(all([true,true]), then={all(true,true)=<<@cond>>})>>
}

ifWithNotAll() ::= {
<<if(all([true,false]), then={},else={all(true,false)=<<@cond>>})>>
}

ifWithNot() ::= {
<<if(not(true), then={}, else={not(true)=<<@cond>>})>>
<<if(not(false), then={not(false)=<<@cond>>}, else={})>>
}

