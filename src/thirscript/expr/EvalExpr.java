package thirscript.expr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import thirscript.IFunction;
import thirscript.ThObject;
import thirscript.Var;

public class EvalExpr implements Expr
{
	final Expr func;
	final List<Expr> args;

	public EvalExpr(Expr func, List<Expr> args)
	{
		this.func = func;
		this.args = new ArrayList<>(args);
	}

	public ThObject eval(Map<String, Var> env)
	{
		ThObject fo = func.eval(env);

		if(!(fo instanceof IFunction))
			throw new UnsupportedOperationException("Cannot call non-function");
		IFunction f = (IFunction) fo;

		List<ThObject> argvs = new ArrayList<>(args.size());
		args.forEach(e -> argvs.add(e.eval(env)));

		if(!f.can_eval(argvs))
			throw new UnsupportedOperationException("Cannot call function " + f + " with " + args);

		return ((IFunction) f).eval(argvs);
	}

	public void addArg(Expr arg)
	{
		args.add(arg);
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder("(");
		sb.append(func);
		for(Expr n: args)
			sb.append(" " + n);
		sb.append(')');

		return sb.toString();
	}
}
