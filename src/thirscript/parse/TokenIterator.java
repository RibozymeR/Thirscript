package thirscript.parse;

import java.io.IOException;

public interface TokenIterator
{
	/**
	 * Returns the next token.
	 *
	 * @return the next token in the iteration
	 * @throws IOException    if an IOException occured while attempting to find token
	 * @throws ParseException if a parsing exception occured while attempting to find token
	 */
	Token next() throws IOException, ParseException;
}
