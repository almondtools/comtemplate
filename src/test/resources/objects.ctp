@definedObject(name, type) ::= [
 name=name,
 type=type
]

@otherObject(description) ::= [
 description=description
]

@subObject(name, type, description) ::= definedObject(name,type)~[description=description]

@subSubObject(name, type, description) ::= subObject(name,type, description)

@sub2Object(name, type, description) ::= definedObject(name,type)~otherObject(description)

printNameType(argument:definedObject) ::= {
argument.name = <<argument.name>>
argument.type = <<argument.type>>
}

objectAttributes() ::= {
<<printNameType(argument=definedObject(name='myname',type='mytype'))>>
}

objectInheritance() ::= {
<<subObject(name='myname',type='mytype',description='mydesc')>>
}

objectTransitiveInheritance() ::= {
<<subSubObject(name='myname',type='mytype',description='mydesc')>>
}

objectMultipleInheritance() ::= {
<<sub2Object(name='myname',type='mytype',description='mydesc')>>
}
