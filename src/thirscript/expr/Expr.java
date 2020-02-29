package thirscript.expr;

import java.util.Map;

import thirscript.ThObject;
import thirscript.Var;

public interface Expr
{
    ThObject eval(Map<String, Var> env);

    String toString();
}
