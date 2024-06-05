package compiler;

import java.util.HashMap;
import java.util.Map;

public class Lexer {
	private int line;
	private int coluna;
	private int position;
	private char chr;
	private String s;

	Map<String, TokenType> keywords = new HashMap<>();

	public Lexer(String source) {
		this.line = 1;
		this.coluna = 0;
		this.position = 0;
		this.s = source;
		this.chr = this.s.charAt(0);
		this.keywords.put("imprimir", TokenType.Reservado_imprimir);
		this.keywords.put("inteiro", TokenType.Reservado_Inteiro);
		this.keywords.put("real", TokenType.Reservado_Real);

	}

	public Token getToken() {
		int line, coluna;
		while (Character.isWhitespace(this.chr)) {
			getNextChar();
		}
		line = this.line;
		coluna = this.coluna;

		switch (this.chr) {
		case '\u0000':
			return new Token(TokenType.End_of_input, "", this.line, this.coluna);
		case '/':
			getNextChar();
			return new Token(TokenType.Op_dividir, "/", line, coluna);
		case '=':
			getNextChar();
			return new Token(TokenType.Op_atribuir, "=", line, coluna);
		case '(':
			getNextChar();
			return new Token(TokenType.ParenEsquerdo, "(", line, coluna);
		case ')':
			getNextChar();
			return new Token(TokenType.ParenDireito, ")", line, coluna);
		case '+':
			getNextChar();
			return new Token(TokenType.Op_somar, "+", line, coluna);
		case '-':
			getNextChar();
			return new Token(TokenType.Op_subtrair, "-", line, coluna);
		case '*':
			getNextChar();
			return new Token(TokenType.Op_multiplicar, "*", line, coluna);
		case ';':
			getNextChar();
			return new Token(TokenType.PontoVirgula, ";", line, coluna);

		default:
			return id_ou_numero(line, coluna);
		}
	}

	public char getNextChar() {
		this.coluna++;
		this.position++;
		if (this.position >= this.s.length()) {
			this.chr = '\u0000';
			return this.chr;
		}
		this.chr = this.s.charAt(this.position);
		if (this.chr == '\n') {
			this.line++;
			this.coluna = 0;
		}
		return this.chr;
	}

	public Token id_ou_numero(int line, int coluna) {
		boolean is_number = true;
		boolean is_float = false;
		String text = "";

		if (Character.isAlphabetic(this.chr)) {
			is_number = false;
			while (Character.isAlphabetic(this.chr) || Character.isDigit(this.chr)) {
				text += this.chr;
				getNextChar();
			}
		} else if (Character.isDigit(this.chr)) {
			while (Character.isDigit(this.chr)) {
				text += this.chr;
				getNextChar();
			}
			if (this.chr == '.') {
				is_float = true;
				text += this.chr;
				getNextChar();
				while (Character.isDigit(this.chr)) {
					text += this.chr;
					getNextChar();
				}
				while (Character.isAlphabetic(this.chr)) {
					text += this.chr;
					getNextChar();
					is_number = false;
				}
			}
			if (Character.isAlphabetic(this.chr)) {
				while (Character.isAlphabetic(this.chr)) {
					text += this.chr;
					getNextChar();
				}
				is_number = false;
			}
		}

		if (text.equals("")) {
			Erro(line, coluna, String.format("Caracter não reconhecido: " + this.chr));
		}

		if (Character.isDigit(text.charAt(0))) {
			if (!is_number) {
				Erro(line, coluna, String.format("Número inválido: " + text));
			}
			if (is_float) {
				return new Token(TokenType.RealConst, text, line, coluna);
			}
			return new Token(TokenType.InteiroConst, text, line, coluna);
		}

		if (this.keywords.containsKey(text)) {
			return new Token(this.keywords.get(text), text, line, coluna);
		}
		return new Token(TokenType.Identificador, text, line, coluna);
	}

	static void Erro(int line, int coluna, String msg) {
		if (line > 0 && coluna > 0) {
			System.out.println(msg + " na linha " + line + " coluna " + coluna);
		} else {
			System.out.println(msg);
		}
		System.exit(1);
	}

	public void printTokens() {
		Token t;
		while ((t = getToken()).tokentype != TokenType.End_of_input) {
			System.out.println(t);
		}
		System.out.println(t);
	}

}