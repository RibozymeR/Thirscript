package thirscript.exec;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BlockExpr implements Expr
{
    final List<Expr> children;

    public BlockExpr(Expr... children)
    {
        this.children = new ArrayList<>(List.of(children));
    }

    public long eval(Map<String, Long> env)
    {
        long v = 0;
        for (Expr n : children)
            v = n.eval(env);
        return v;
    }

    public void addChild(Expr c)
    {
        children.add(c);
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder("(body ");
        for (Expr n : children)
            sb.append(" " + n);
        sb.append(')');

        return sb.toString();
    }
}
