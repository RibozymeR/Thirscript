package thirscript.expr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import thirscript.ThObject;
import thirscript.Var;

public class BlockExpr implements Expr
{
    final List<Expr> children;

    public BlockExpr(Expr... children)
    {
        this.children = new ArrayList<>(List.of(children));
    }

    public ThObject eval(Map<String, Var> env)
    {
        Map<String, Var> nenv = new HashMap<>(env);
        // TODO replace null
        ThObject v = null;
        for (Expr n : children)
            v = n.eval(nenv);
        for (String var : env.keySet())
            if (nenv.get(var) != env.get(var)) env.put(var, nenv.get(var));
        return v;
    }

    public void addChild(Expr c)
    {
        children.add(c);
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder("(body");
        for (Expr n : children)
        {
            sb.append(' ');
            sb.append(n);
        }
        sb.append(')');

        return sb.toString();
    }
}
