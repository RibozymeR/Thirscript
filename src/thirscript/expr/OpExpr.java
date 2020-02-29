package thirscript.expr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PrimitiveIterator.OfLong;
import java.util.stream.Collectors;

import thirscript.ThInteger;
import thirscript.ThObject;
import thirscript.Var;

public class OpExpr implements Expr
{
    final List<Expr> args;
    final List<Character> ops;

    public OpExpr(Expr first)
    {
        args = new ArrayList<>(List.of(first));
        ops = new ArrayList<>();
    }

    public ThObject eval(Map<String, Var> env)
    {
        List<ThObject> arg_objs = args.stream().map(e -> e.eval(env)).collect(Collectors.toList());

        if (arg_objs.stream().allMatch(obj -> obj instanceof ThInteger)) return evalInts(arg_objs);

        // TODO use operand methods
        return null;
    }

    public ThObject evalInts(List<ThObject> int_objs)
    {
        OfLong ints = int_objs.stream().mapToLong(o -> ((ThInteger) o).value).iterator();

        long v = ints.nextLong();
        for (int i = 0; i < ops.size(); ++i)
        {
            char op = ops.get(i);
            long b = ints.nextLong();
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
        return ThInteger.valueOf(v);
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
