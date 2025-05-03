package thirscript.expr;

import java.util.Map;

import thirscript.ThInteger;
import thirscript.ThObject;
import thirscript.Var;

public class IntLiteral implements Expr
{
	final long value;

	public IntLiteral(long value)
	{
		this.value = value;
	}

	public IntLiteral(ThInteger value)
	{
		this.value = value.value;
	}

	public ThObject eval(Map<String, Var> env)
	{
		return ThInteger.valueOf(value);
	}

	public String toString()
	{
		return Long.toString(value);
	}
}
