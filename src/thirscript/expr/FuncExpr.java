package thirscript.expr;

import java.util.List;
import java.util.Map;

import thirscript.ThFunction;
import thirscript.ThObject;
import thirscript.Var;

//#( [ IDENTIFIER { "," IDENTIFIER } ] ) cmp
public class FuncExpr implements Expr
{
    final List<String> arg_names;
    final Expr body;

    public FuncExpr(List<String> arg_names, Expr body)
    {
        if (arg_names.contains(null) || arg_names.contains("") || arg_names.contains("_"))
            throw new IllegalArgumentException("Function expression contains invalid argument name");
        this.arg_names = arg_names;
        this.body = body;
    }

    // TODO: closure
    public ThObject eval(Map<String, Var> env)
    {
        return new ThFunction(body, arg_names);
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder("(lambda (");
        if (!arg_names.isEmpty())
        {
            sb.append(arg_names.get(0));
            for (int i = 1; i < arg_names.size(); ++i)
            {
                sb.append(' ');
                sb.append(arg_names.get(i));
            }
        }
        sb.append(") ");
        sb.append(body);
        sb.append(')');
        return sb.toString();
    }
}
