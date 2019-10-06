package thirscript.exec;

import java.util.Map;

public class PrintExpr implements Expr
{
    final Expr arg;

    public PrintExpr(Expr arg)
    {
        this.arg = arg;
    }

    public long eval(Map<String, Long> env)
    {
        System.out.println(arg.eval(env));
        return -1;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder("(print ");
        sb.append(arg);
        sb.append(')');

        return sb.toString();
    }
}
