import html.*

data ::= [1, "", 2]

_name(data) ::= "source"~data~".html"

_main(data) ::= {
<<html(content={
  <<head(content={
  })>>
  <<body(content={
    <<h1(content={Hello World})>>
    <<p(content={Some Text:}~data)>>
  })>>
})>>
}