package thirscript.expr;

import java.util.List;
import java.util.Map;

public class UnaryOpExpr implements Expr
{
    final Expr arg;
    final List<Character> ops;

    public UnaryOpExpr(Expr arg, List<Character> ops)
    {
        this.arg = arg;
        this.ops = ops;
    }

    public long eval(Map<String, Long> env)
    {
        long v = arg.eval(env);
        for (char op : ops)
            switch (op) {
            case '+':
                break;
            case '-':
                v = -v;
                break;
            case '~':
                v = ~v;
                break;
            case '!':
                v = v == 0 ? -1 : 0;
            }
        return v;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder("(op ");
        for (char op : ops)
            sb.append(op);
        sb.append(' ');
        sb.append(arg);
        sb.append(')');

        return sb.toString();
    }
}
