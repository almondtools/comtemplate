concatLists() ::= {
`call2lists([1,2],[3,4])`
}

concatMaps() ::= {
`call2maps([a=1],[b=2])`
}

call2lists(l1,l2) ::= {`call1list(@l1~'|'~@l2)`}

call1list(list) ::= {`list.separated(' ')`}

call2maps(m1,m2) ::= {`call1map(@m1~@m2)`}

call1map(map) ::= {`map.keys().separated(' ')` -> `map.values().separated(' ')`}
