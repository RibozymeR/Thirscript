    block = { cmp } ;

    expr = { "+" | "-" | "~" | "!" } , (
        "(" , cmp , ")" , [ eval ]
        | "{" , block , "}"
        | INT
        | STRING
        | lambda
        | new_obj
        | var , [ ( "=" | ":=" ) , cmp | eval ]
        | if_expr
        | while_expr
    ) ;
    eval = "(" , [ cmp , { "," , cmp } ] , ")" ;
    
    lambda = "#" , "(" , [ IDENTIFIER , { "," , IDENTIFIER } ] , ")" , expr ;         # function arguments cannot be called "_"
    new_obj = "new" , expr , "{" , { var , ( "=" | ":=" ) , cmp } , "}" ;

    var = IDENTIFIER , { '.' , IDENTIFIER } ;

    if_expr = "if" , "(" , cmp , ")" , cmp , [ "else" , cmp ] ;
    while_expr = "while" , "(" , cmp , ")" , cmp ;
    
    cmp = sum , [ CMP_OP , sum ] ;
    sum = product , { SUM_OP , product } ;
    product = expr , { PROD_OP , expr } ;
        
    IDENTIFIER = [_a-zA-Z][_0-9a-zA-Z]*
    
    INT = 0 | [1-9][0-9]*
    STRING = '[^']*'
    
    CMP_OP = [<>] | [=!<>]=
    SUM_OP = [+-&|^]
    PROD_OP = [*/%]