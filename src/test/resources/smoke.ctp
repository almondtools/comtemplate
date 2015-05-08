object(field1, field2=[]) ::= [
  field1=field1,
  field2=field2,
  field3='field3'
]

map ::= [
 entry1 = '1',
 entry2 = '2'
]
 
list ::= ['1','2']

intconst ::= 1
decconst ::= -0.2
boolconst ::= false
strconst ::= 'string'

html(content='',attributes=[]) ::= {
  <html `for(var=attributes,action=var,separator=' ')`>`content`</html>
}
