package thirscript.parse;

import java.util.Arrays;

public class Token
{
    public static final Token EOF = new Token(TokenType.EOF, -1);

    final TokenType type;
    final int pos;
    final String value;

    protected Token(TokenType type, int pos)
    {
        this(type, pos, null);
    }

    protected Token(TokenType type, int pos, String value)
    {
        if (type == TokenType.IDENTIFIER && Arrays.binarySearch(TokenType.reserved, value) >= 0)
            throw new IllegalArgumentException("Identifier token (" + pos + ") cannot be keyword");

        this.type = type;
        this.pos = pos;
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
        if (value != null)
            return type.toString() + "[" + value + "]";
        else
            return type.toString();
    }

    public static enum TokenType {
        IDENTIFIER, /* identifier */
        PERIOD, /* '.' */
        INT, /* int literal */
        PRINT, /* print */
        ASSIGN, /* '=' */
        IF, ELSE, WHILE, /* keywords */
        OP, /* + - * / % & | ^ ~ == < <= > >= != */
        LPAREN, RPAREN, LBRACE, RBRACE, EOF;

        public static String[] reserved = { "else", "if", "print", "while" };
    }
}
