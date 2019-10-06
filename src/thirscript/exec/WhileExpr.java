package thirscript.exec;

import java.util.Map;

public class WhileExpr implements Expr
{
    final Expr test, body;

    public WhileExpr(Expr test, Expr body)
    {
        this.test = test;
        this.body = body;
    }

    public long eval(Map<String, Long> env)
    {
        long v = 0L;
        while (test.eval(env) != 0)
            v = body.eval(env);
        return v;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder("(while ");
        sb.append(test);
        sb.append(' ');
        sb.append(body);
        sb.append(')');
        return sb.toString();
    }
}