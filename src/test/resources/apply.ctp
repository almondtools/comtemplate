myobject(name, type) ::= [
 name=name,
 type=type
]

mytemplate(content) ::= {
`content`
}

applyObject() ::= {
`apply(name="myobject",arguments=[name='myname',type='mytype']).name`
}

applyTemplate() ::= {
`apply(name="mytemplate",arguments=[content='mycontent'])`
}
