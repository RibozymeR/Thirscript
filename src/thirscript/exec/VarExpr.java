package thirscript.exec;

import java.util.Map;

public class VarExpr implements Expr
{
    final String var;

    public VarExpr(String var)
    {
        this.var = var;
    }

    public long eval(Map<String, Long> env)
    {
        return env.get(var);
    }

    public void set(long value, Map<String, Long> env)
    {
        env.put(var, value);
    }

    public String toString()
    {
        return var;
    }
}
