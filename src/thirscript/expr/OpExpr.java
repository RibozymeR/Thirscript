package thirscript.expr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OpExpr implements Expr
{
    final List<Expr> args;
    final List<Character> ops;

    public OpExpr(Expr first)
    {
        args = new ArrayList<>(List.of(first));
        ops = new ArrayList<>();
    }

    public long eval(Map<String, Long> env)
    {
        long v = args.get(0).eval(env);
        for (int i = 0; i < ops.size(); ++i)
        {
            char op = ops.get(i);
            long b = args.get(i + 1).eval(env);
            switch (op) {
            case '+':
                v += b;
                break;
            case '-':
                v -= b;
                break;
            case '&':
                v &= b;
                break;
            case '|':
                v |= b;
                break;
            case '^':
                v ^= b;
                break;
            case '*':
                v *= b;
                break;
            case '/':
                v /= b;
                break;
            case '%':
                v %= b;
            }
        }
        return v;
    }

    public Expr compress()
    {
        if (args.size() == 1)
            return args.get(0);
        else
            return this;
    }

    public void addChild(Expr c, char op)
    {
        args.add(c);
        ops.add(op);
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder("(op ");
        sb.append(args.get(0));
        for (int i = 0; i < ops.size(); ++i)
        {
            sb.append(' ');
            sb.append(ops.get(i));
            sb.append(args.get(i + 1));
        }
        sb.append(')');

        return sb.toString();
    }
}
