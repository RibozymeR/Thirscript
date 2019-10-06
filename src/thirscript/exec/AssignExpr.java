package thirscript.exec;

import java.util.Map;

public class AssignExpr implements Expr
{
    final VarExpr var;
    final Expr value;

    public AssignExpr(VarExpr var, Expr value)
    {
        this.var = var;
        this.value = value;
    }

    public long eval(Map<String, Long> env)
    {
        long v = value.eval(env);
        var.set(v, env);
        return v;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder("(= ");
        sb.append(var);
        sb.append(' ');
        sb.append(value);
        sb.append(')');

        return sb.toString();
    }
}
