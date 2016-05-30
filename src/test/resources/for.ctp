forInList() ::= {
  <<for(number=['1','2'], do={ <<@number>>:<<@number>>})>>
}

forInDefault() ::= {
  <<for(['a','b'], do={:<<@item>>})>>
}

forInListI0() ::= {
  <<for(char=['a','b'], do={ <<@i0>>:<<@char>>})>>
}

forInListI() ::= {
  <<for(char=['a','b'], do={ <<@i>>:<<@char>>})>>
}