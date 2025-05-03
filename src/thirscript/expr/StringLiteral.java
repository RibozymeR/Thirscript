package thirscript.expr;

import java.util.Map;

import thirscript.ThObject;
import thirscript.ThString;
import thirscript.Var;

public class StringLiteral implements Expr
{
	final String value;

	public StringLiteral(String value)
	{
		this.value = value;
	}

	public ThObject eval(Map<String, Var> env)
	{
		return ThString.valueOf(value);
	}

	public String toString()
	{
		return String.format("'%s'", value);
	}
}
