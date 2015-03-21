map ::= [
 name='name',
 type='type'
]

duplicatemap ::= [
 key='name',
 type='type',
 key='value'
]

list ::= ['element1', 'element2']

int ::= 22

dec ::= -0.2

bool ::= true

rendermap() ::= {
map = [name=@map.name,type=@map.type]
}

renderduplicatemap() ::= {
map = [name=@duplicatemap.key,type=@duplicatemap.type]
}

renderlist() ::= {
list = [@list.separated(',')]
}

renderint() ::= {
int = @int
}

renderdec() ::= {
dec = @dec
}

renderbool() ::= {
bool = @bool.not()
}