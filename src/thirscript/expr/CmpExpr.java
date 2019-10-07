package thirscript.expr;

import java.util.Map;

public class CmpExpr implements Expr
{
    final Expr left, right;
    final String cmp;

    public CmpExpr(Expr left, Expr right, String op)
    {
        this.left = left;
        this.right = right;
        cmp = op;
    }

    public long eval(Map<String, Long> env)
    {
        long l = left.eval(env), r = right.eval(env);
        int cmp_ix = "== < <= > >= !=".indexOf(cmp);

        boolean b = false;
        switch (cmp_ix) {
        case 0:
            b = l == r;
            break;
        case 3:
            b = l < r;
            break;
        case 5:
            b = l <= r;
            break;
        case 8:
            b = l > r;
            break;
        case 10:
            b = l >= r;
            break;
        case 13:
            b = l != r;
        }

        return b ? -1 : 0;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder("(");
        sb.append(cmp);
        sb.append(' ');
        sb.append(left);
        sb.append(' ');
        sb.append(right);
        sb.append(')');

        return sb.toString();
    }
}
