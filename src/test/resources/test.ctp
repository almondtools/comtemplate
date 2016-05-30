html(content) ::= {
<html><<content>></html>
}

test ::= html("content").equals("<html>content</html>")
 
fail ::= html("content").equals("<html>\ncontent</html>")
