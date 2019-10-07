package thirscript;

import java.io.InputStreamReader;
import java.util.HashMap;

import thirscript.expr.Expr;
import thirscript.parse.Lexer;
import thirscript.parse.Parser;

public class Thirscript
{
    public static void main(String[] args)
    {
        Lexer lexer = new Lexer(new InputStreamReader(System.in));
        Parser parser = new Parser();

        Expr n = parser.parse(lexer.iterator());
        System.out.println(n);
        System.out.println(n.eval(new HashMap<>()));
    }
}