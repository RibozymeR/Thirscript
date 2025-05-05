package thirscript.test;

import java.io.IOException;
import java.io.PrintStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import thirscript.JavaFunction;
import thirscript.ThInteger;
import thirscript.ThObject;
import thirscript.Var;
import thirscript.expr.Expr;
import thirscript.parse.Lexer;
import thirscript.parse.ParseException;
import thirscript.parse.Parser;
import thirscript.parse.Token;
import thirscript.parse.Token.TokenType;

public class Test
{
	public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, IOException, ParseException
	{
		Lexer lexer = new Lexer(Files.newBufferedReader(Paths.get("test.th")));
		while(true) {
			Token token = lexer.next();
			System.out.println(token);
			
			if(token.type() == TokenType.EOF)
				break;
		}
		
		lexer = new Lexer(Files.newBufferedReader(Paths.get("test.th")));
		Parser parser = new Parser();

		Expr n = parser.parse(lexer);
		System.out.println(n);

		Map<String, Var> start = new HashMap<>();

		// put builtin "types"
		start.put("Object", Var.constant(ThObject.OBJECT));

		// put builtin values
		start.put("nil", ThObject.NULL);
		start.put("false", Var.constant(ThInteger.FALSE));
		start.put("true", Var.constant(ThInteger.TRUE));

		// put print
		{
			MethodType print_t = MethodType.methodType(void.class, Object.class);
			MethodHandle print = MethodHandles.lookup().findVirtual(PrintStream.class, "println", print_t);
			print = MethodHandles.insertArguments(print, 0, System.out);
			print = print.asType(print_t.changeParameterType(0, ThObject.class));
			print = MethodHandles.filterReturnValue(print, MethodHandles.constant(ThObject.class, ThInteger.TRUE));
			start.put("print", Var.constant(new JavaFunction(print)));
		}

		System.out.println(n.eval(start));
	}
}
