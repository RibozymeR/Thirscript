package thirscript.parse;

import java.util.Arrays;

public class Token
{
	public static final Token EOF = new Token(TokenType.EOF, -1, -1);

	final TokenType type;
	final int line, column;
	final String value;

	protected Token(TokenType type, int line, int column)
	{
		this(type, line, column, null);
	}

	protected Token(TokenType type, int line, int column, String value)
	{
		if(type == TokenType.IDENTIFIER && Arrays.binarySearch(TokenType.reserved, value) >= 0)
			throw new IllegalArgumentException("Identifier token at " + line + ":" + column + " cannot be keyword");

		this.type = type;
		this.line = line + 1;
		this.column = column + 1;
		this.value = value;
	}

	public TokenType getType()
	{
		return type;
	}

	public Object getValue()
	{
		return value;
	}

	public String toString()
	{
		if(value != null)
			return type.toString() + "[" + value + "]";
		else
			return type.toString();
	}

	public String getPos()
	{
		return String.format("%d:%d", line, column);
	}

	public static enum TokenType
	{
		IDENTIFIER, /* identifier */
		PERIOD, /* '.' */
		COMMA, /* ',' */
		INT, /* int literal */
		STRING, /* string literal */
		FUNC, /* '#' */
		ASSIGN, ASSIGNC, /* '=', ':=' */
		IF, ELSE, WHILE, NEW, /* keywords */
		OP, /* + - * / % & | ^ ~ == < <= > >= != */
		LPAREN, RPAREN, LBRACE, RBRACE, EOF;

		public static String[] reserved = {"else", "if", "new", "while"};
	}
}
