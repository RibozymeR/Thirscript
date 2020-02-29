package thirscript.expr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import thirscript.ThObject;
import thirscript.Var;

public class NewExpr implements Expr
{
    final List<Expr> protos;
    final List<AssignExpr> new_vars;
    private final Set<String> var_names;

    public NewExpr(List<Expr> protos, List<AssignExpr> var_exprs)
    {
        this.protos = protos;
        this.new_vars = var_exprs;
        var_names = new HashSet<>();
        for (AssignExpr e : new_vars)
            var_names.add(e.var.var);
    }

    public ThObject eval(Map<String, Var> env)
    {
        List<ThObject> protos = new ArrayList<>(this.protos.size());
        for (Expr src : this.protos)
            protos.add(src.eval(env));

        Map<String, Var> new_env = new HashMap<>(env);
        for (AssignExpr assexpr : new_vars)
            assexpr.eval(new_env);
        new_env.keySet().retainAll(var_names);

        return new ThObject(protos, new_env);
    }

    public void addSource(Expr src)
    {
        protos.add(src);
    }

    public void addVar(AssignExpr ass)
    {
        new_vars.add(ass);
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder("(new (");
        sb.append(protos.get(0));
        for (int i = 1; i < protos.size(); ++i)
        {
            sb.append(' ');
            sb.append(protos.get(i));
        }
        sb.append(')');
        for (AssignExpr ass : new_vars)
        {
            sb.append(' ');
            sb.append(ass);
        }
        sb.append(')');
        return sb.toString();
    }
}
