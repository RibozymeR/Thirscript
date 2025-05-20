package thirscript.parse;

import java.util.Set;

public record Token(TokenType type, String content, int line, int column)
{

	public static final Token EOF = new Token(TokenType.EOF, -1, -1);

	public Token
	{
		if(type == TokenType.IDENTIFIER && TokenType.reserved.contains(content))
			throw new IllegalArgumentException("Identifier token at " + line + ":" + column + " should be keyword");
	}

	public Token(TokenType type, int line, int column)
	{
		this(type, null, line, column);
	}

	public String toString()
	{
		if(content != null)
			return type.toString() + "[" + content + "]";
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
		PERIOD, /* . */
		COMMA, /* , */
		INT, /* int literal */
		STRING, /* string literal */
		FUNC, /* # */
		ASSIGN, ASSIGNC, /* = := */
		IF, ELSE, WHILE, NEW, /* keywords */
		OP, /* + - * / % & | ^ ~ == < <= > >= != */
		LPAREN, RPAREN, LBRACE, RBRACE, /* ( ) { } */
		EOF; /* signals end of file/input */

		public static final Set<String> reserved = Set.of("if", "else", "while", "new");
	}
}
