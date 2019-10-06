package thirscript.exec;

import java.util.Map;

public class IfExpr implements Expr
{
    final Expr test, if_node, else_node;

    public IfExpr(Expr test, Expr if_node)
    {
        this(test, if_node, null);
    }

    public IfExpr(Expr test, Expr if_node, Expr else_node)
    {
        this.test = test;
        this.if_node = if_node;
        this.else_node = else_node;
    }

    public long eval(Map<String, Long> env)
    {
        long t = test.eval(env);
        return t != 0 ? if_node.eval(env) : else_node != null ? else_node.eval(env) : 0L;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder("(if ");
        sb.append(test + " ");
        sb.append(if_node);
        if (else_node != null) sb.append(" " + else_node);
        sb.append(')');

        return sb.toString();
    }
}