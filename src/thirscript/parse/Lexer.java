package thirscript.parse;

import static thirscript.parse.Token.TokenType.COMMA;
import static thirscript.parse.Token.TokenType.ELSE;
import static thirscript.parse.Token.TokenType.FUNC;
import static thirscript.parse.Token.TokenType.IDENTIFIER;
import static thirscript.parse.Token.TokenType.IF;
import static thirscript.parse.Token.TokenType.LBRACE;
import static thirscript.parse.Token.TokenType.LPAREN;
import static thirscript.parse.Token.TokenType.NEW;
import static thirscript.parse.Token.TokenType.PERIOD;
import static thirscript.parse.Token.TokenType.RBRACE;
import static thirscript.parse.Token.TokenType.RPAREN;
import static thirscript.parse.Token.TokenType.WHILE;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Map;

import thirscript.parse.Token.TokenType;

public class Lexer implements TokenIterator
{
	/// source for the lexer to read from
	Reader source;
	/// current line and column
	int line, column;

	/// last read character
	int cur;

	/**
	 * Creates a new lexer.
	 *
	 * @param source Source code to be lexed.
	 */
	public Lexer(Reader source)
	{
		this.source = source;
		this.line = 1;
		this.column = 0; // little hack: start this at 0 and cur as ' ' so that the first actual character of the source code is 1:1

		cur = ' ';
	}

	/**
	 * Creates a new lexer.
	 *
	 * @param source Source code to be lexed.
	 */
	public Lexer(String source)
	{
		this(new StringReader(source));
	}

	/**
	 * Creates a new lexer.
	 *
	 * @param source Source code to be lexed.
	 */
	public Lexer(InputStream source)
	{
		this(new InputStreamReader(source));
	}

	/**
	 * Create a token with the given type, at the current position.
	 */
	private Token token(TokenType type)
	{
		return new Token(type, line, column);
	}

	/**
	 * Create a token with the given type and content, at the current position.
	 */
	private Token token(TokenType type, String content)
	{
		return new Token(type, content, line, column);
	}

	/**
	 * Tokens completely determined by a single character.
	 */
	private static final Map<Character, TokenType> single_char_types = Map.of('.', PERIOD, ',', COMMA, '#', FUNC, '(', LPAREN, ')', RPAREN, '{', LBRACE, '}',
			RBRACE);

	/**
	 * Keywords
	 */
	private static final Map<String, TokenType> keyword_types = Map.of("if", IF, "else", ELSE, "while", WHILE, "new", NEW);

	/**
	 * Read one character from the underlying Reader, advance line and column count as appropriate.
	 * 
	 * @throws IOException In case of an IOException in the underlying Reader.
	 */
	private void read() throws IOException
	{
		if(cur == '\n') {
			line++;
			column = 1;
		}
		else
			column++;

		cur = source.read();
	}

	@Override
	/**
	 * Read and return one token from the source code, skipping over any whitespace.
	 * 
	 * @return The next token in the source code. If all source code has been read, <code>Token.EOF</code> is returned.
	 * 
	 * @throws IOException    In case an IOException occured while reading the source.
	 * @throws ParseException If it was not possible to parse the next token.
	 */
	public Token next() throws IOException, ParseException
	{
		// skip whitespace
		while(cur != -1 && Character.isWhitespace(cur)) {
			read();
		}

		// at the end, just return EOF
		if(cur == -1)
			return Token.EOF;

		// ' introduces string
		if(cur == '\'') {
			StringBuilder string = new StringBuilder();

			read();

			while(cur != -1 && cur != '\'') {
				string.append((char) cur);
				read();
			}

			if(cur == -1)
				throw new IOException("Unexpected end of file!");

			read();

			return token(TokenType.STRING, string.toString());
		}

		// digit means we have a number
		if(Character.isDigit(cur)) {
			StringBuilder token = new StringBuilder();

			while(Character.isDigit(cur)) {
				token.append((char) cur);
				read();
			}

			if(Character.isAlphabetic(cur))
				throw new ParseException("Invalid digit followed by letter at " + line + ":" + column);

			return token(TokenType.INT, token.toString());
		}

		// letter or _ means we have an identifier or a keyword
		if(Character.isLetter(cur) || cur == '_') {
			StringBuilder token_builder = new StringBuilder();

			while(Character.isLetterOrDigit(cur) || cur == '_') {
				token_builder.append((char) cur);
				read();
			}

			String token = token_builder.toString();
			return token(keyword_types.getOrDefault(token, IDENTIFIER), token);
		}

		// trivial cases
		if(single_char_types.containsKey((char) cur)) {
			int c = cur;

			read();

			return token(single_char_types.get((char) c));
		}

		// handle two-char operators
		if(cur == ':' || cur == '<' || cur == '>' || cur == '!' || cur == '=') {
			int first = cur;

			read();

			if(cur == '=') {
				read();
				return first == ':' ? token(TokenType.ASSIGNC) : token(TokenType.OP, String.format("%c=", (char) first));
			}
			else
				return first == '=' ? token(TokenType.ASSIGN) : token(TokenType.OP, Character.toString(first));
		}

		// if nothing above applies, we return a one-character operator
		int op = cur;

		read();

		return token(TokenType.OP, Character.toString(op));
	}
}
