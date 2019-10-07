package thirscript.expr;

import java.util.Map;

public class IntLiteral implements Expr
{
    final long value;

    public IntLiteral(long value)
    {
        this.value = value;
    }

    public long eval(Map<String, Long> env)
    {
        return value;
    }

    public String toString()
    {
        return Long.toString(value);
    }
}
