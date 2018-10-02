validbodysyntax1() ::= {
    <<atemplate1()>>
    <<atemplate2()>>
    <<atemplate3()>>
}

validbodysyntax2() ::= {
    <<atemplate1()>>
    <<atemplate2()>>
    <<atemplate3()>>
}

invalidbodysyntax1() ::= {
    <<atemplate1()>>
    <<atemplate2()>
    <<atemplate3()>>
}

invalidbodysyntax2() ::= {
    <<atemplate1()>>
    <<abadtemplate2()>>
    <<atemplate3()>>
}

invalidbodysyntax3() ::= {
    <<nest({
        <<nest({
            <<atemplate2()>
        })>>
    }, attributes=[atemplate1()])>>
}

nest(body, attributes) ::= {
    <<body>>
}

atemplate1() ::= {
}

atemplate2() ::= {
}

atemplate3() ::= {
}

abadtemplate2() ::= {
    <<atemplate2()>
}