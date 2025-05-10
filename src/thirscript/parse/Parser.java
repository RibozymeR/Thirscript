package thirscript.parse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import thirscript.expr.AssignExpr;
import thirscript.expr.BlockExpr;
import thirscript.expr.CmpExpr;
import thirscript.expr.EvalExpr;
import thirscript.expr.Expr;
import thirscript.expr.FuncExpr;
import thirscript.expr.IfExpr;
import thirscript.expr.IntLiteral;
import thirscript.expr.NewExpr;
import thirscript.expr.OpExpr;
import thirscript.expr.StringLiteral;
import thirscript.expr.UnaryOpExpr;
import thirscript.expr.VarExpr;
import thirscript.expr.WhileExpr;
import thirscript.parse.Token.TokenType;

public class Parser
{
	private Token current_token;
	private TokenIterator token_iter;

	// EVAL

	// parses expressions until EOF or }
	public BlockExpr parse() throws IOException, ParseException
	{
		BlockExpr main = new BlockExpr();
		while(get() != Token.EOF && get().type() != TokenType.RBRACE)
			main.addChild(parseCmp());
		return main;
	}

	// parses expression at current position
	// will end with pointer directly after expression
	public Expr parseExpr() throws IOException, ParseException
	{
		List<Character> pre_op = new ArrayList<>();
		Token t;
		while((t = get()).type() == TokenType.OP && "+ - ~ !".indexOf((String) t.value()) >= 0) {
			next();
			pre_op.add(t.value().charAt(0));
		}

		Expr n;

		boolean can_eval = false;

		t = get();
		TokenType type = t.type();
		String value = t.value();
		if(type == TokenType.LPAREN) {
			next();
			n = parseCmp();
			assertType(next(), TokenType.RPAREN);
			can_eval = true;
		}
		else if(type == TokenType.LBRACE) {
			next();
			n = parse();
			assertType(next(), TokenType.RBRACE);
		}
		else if(type == TokenType.INT) {
			next();
			n = new IntLiteral(Long.parseLong(value));
		}
		else if(type == TokenType.STRING) {
			next();
			n = new StringLiteral(value);
		}
		else if(type == TokenType.IDENTIFIER) {
			VarExpr v = parseVar();
			TokenType asstype = get().type();
			if(asstype == TokenType.ASSIGN || asstype == TokenType.ASSIGNC) {
				next();
				n = new AssignExpr(v, parseCmp(), asstype == TokenType.ASSIGNC);
			}
			else {
				n = v;
				can_eval = true;
			}
		}
		else if(type == TokenType.FUNC) {
			next();
			assertType(next(), TokenType.LPAREN);
			List<String> arg_names = new ArrayList<>();
			if(get().type() == TokenType.IDENTIFIER) {
				arg_names.add(next().value());
				while(get().type() == TokenType.COMMA) {
					next();
					assertType(get(), TokenType.IDENTIFIER);
					arg_names.add(next().value());
				}
			}
			assertType(next(), TokenType.RPAREN);
			n = new FuncExpr(arg_names, parseCmp());
		}
		else if(type == TokenType.IF) {
			next();
			assertType(next(), TokenType.LPAREN);
			Expr test = parseCmp();
			assertType(next(), TokenType.RPAREN);

			Expr do_if = parseCmp();

			if(get().type() == TokenType.ELSE) {
				next();
				n = new IfExpr(test, do_if, parseCmp());
			}
			else
				n = new IfExpr(test, do_if);
		}
		else if(type == TokenType.WHILE) {
			next();
			assertType(next(), TokenType.LPAREN);
			Expr test = parseCmp();
			assertType(next(), TokenType.RPAREN);

			n = new WhileExpr(test, parseCmp());
		}
		else if(type == TokenType.NEW) {
			// "new" , expr , "{" , { var , ( "=" | ":=" ) , cmp } , "}" ;
			next();
			Expr proto;
			if(get().type() != TokenType.LBRACE)
				proto = parseExpr();
			else
				proto = new VarExpr("Object", null);

			assertType(next(), TokenType.LBRACE);
			List<AssignExpr> assigns = new ArrayList<>();
			while(get().type() != TokenType.RBRACE) {
				VarExpr v = parseVar();
				TokenType asstype = get().type();
				if(asstype == TokenType.ASSIGN || asstype == TokenType.ASSIGNC) {
					next();
					assigns.add(new AssignExpr(v, parseCmp(), asstype == TokenType.ASSIGNC));
				}
				else
					throw new ParseException("Token at " + get().getPos() + " (" + asstype + ") should be " + TokenType.ASSIGN + " or " + TokenType.ASSIGNC);
			}
			next();

			n = new NewExpr(proto, assigns);

		}
		else
			throw new ParseException("Not a valid statement at " + t.getPos());

		if(can_eval && get().type() == TokenType.LPAREN) {
			next();
			List<Expr> args = new ArrayList<>();
			if(get().type() != TokenType.RPAREN) {
				args.add(parseCmp());
				while(get().type() == TokenType.COMMA) {
					next();
					args.add(parseCmp());
				}
				assertType(next(), TokenType.RPAREN);
			}
			else
				next();
			n = new EvalExpr(n, args);
		}

		if(pre_op.size() > 0) {
			Collections.reverse(pre_op);
			n = new UnaryOpExpr(n, pre_op);
		}

		return n;
	}

	public Expr parseCmp() throws IOException, ParseException
	{
		Expr n = parseSum();

		Token t = get();
		if(t.type() == TokenType.OP) {
			if("== < <= > >= !=".indexOf(t.value()) == -1)
				throw new ParseException("Not a valid operator at " + t.getPos());
			next();
			return new CmpExpr(n, parseSum(), t.value());
		}
		else
			return n;
	}

	private Expr parseSum() throws IOException, ParseException
	{
		OpExpr n = new OpExpr(parseProd());
		Token t;
		while((t = get()).type() == TokenType.OP && "+-&|^".indexOf(t.value()) != -1) {
			next();
			n.addChild(parseProd(), t.value().charAt(0));
		}
		return n.compress();
	}

	private Expr parseProd() throws IOException, ParseException
	{
		OpExpr n = new OpExpr(parseExpr());
		Token t;
		while((t = get()).type() == TokenType.OP && "*/%".indexOf(t.value()) != -1) {
			next();
			n.addChild(parseExpr(), t.value().charAt(0));
		}
		return n.compress();
	}

	private VarExpr parseVar() throws IOException, ParseException
	{
		Token t = next();
		assertType(t, TokenType.IDENTIFIER);
		String var = t.value();
		List<String> path = new ArrayList<>();
		while(get().type() == TokenType.PERIOD) {
			next();
			t = next();
			assertType(t, TokenType.IDENTIFIER);
			path.add(t.value());
		}
		return new VarExpr(var, path);
	}

	// MISC

	// current token
	private Token get()
	{
		return current_token;
	}

	// current token, also advances pointer
	private Token next() throws IOException, ParseException
	{
		Token t = current_token;
		current_token = token_iter.next();
		return t;
	}

	private boolean assertType(Token token, TokenType type) throws ParseException
	{
		if(token.type() == type)
			return true;
		else
			throw new ParseException("Token at " + token.getPos() + " (" + token.type() + ") should be " + type);
	}

	public Expr parse(TokenIterator tokens) throws IOException, ParseException
	{
		token_iter = tokens;
		current_token = token_iter.next();

		return parse();
	}
}
