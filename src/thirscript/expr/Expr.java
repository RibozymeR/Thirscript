package thirscript.expr;

import java.util.Map;

public interface Expr
{
    long eval(Map<String, Long> env);

    String toString();
}
