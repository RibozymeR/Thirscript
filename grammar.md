    program = { cmp } ;

    expr = { "+" | "-" | "~" | "!" } , (
        "(" , cmp , ")"
        | "{" , { cmp } , "}"
        | INT
        | "#" , "(" , [ IDENTIFIER , { "," , IDENTIFIER } ] , ")" , cmp
        | var , [ "=" , cmp ]
        | ( var | "(" , cmp , ")" ) , "(" , [ cmp , { "," , cmp } ] , ")"
        | if_expr
        | while_expr
    ) ;

    var = IDENTIFIER ;

    if_expr = "if" , "(" , cmp , ")" , cmp , [ "else" , cmp ] ;
    while_expr = "while" , "(" , cmp , ")" , cmp ;
    
    cmp = sum , [ CMP_OP , sum ] ;
    sum = product , { SUM_OP , product } ;
    product = expr , { PROD_OP , expr } ;
        
    IDENTIFIER = [_a-zA-Z][_0-9a-zA-Z]*
    
    INT = 0 | [1-9][0-9]*
    
    CMP_OP = [<>] | [=!<>]=
    SUM_OP = [+-&|^]
    PROD_OP = [*/%]