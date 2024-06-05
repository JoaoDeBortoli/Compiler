package compiler;

public class Token {
	public TokenType tokentype;
	public String value;
	public int line;
	public int pos;

	public Token(TokenType token, String value, int line, int pos) {
		this.tokentype = token;
		this.value = value;
		this.line = line;
		this.pos = pos;
	}

	@Override
	public String toString() {
		String result = String.format("%5d  %5d %-15s", this.line, this.pos, this.tokentype);
		switch (this.tokentype) {
		case InteiroConst:
			result += String.format("  %4s", value);
			break;
		case RealConst:
			result += String.format("  %4s", value);
			break;
		case Identificador:
			result += String.format(" %s", value);
			break;
		default:
			break;
		}
		return result;
	}
}
