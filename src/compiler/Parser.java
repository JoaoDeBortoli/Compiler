package compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Parser {
	private List<Token> source;
	
//  utilizado para verificar se o identificador tem nome unico
	private ArrayList <Token> declarations = new ArrayList<Token>();

// 	utilizado para verificar se o identificador foi declarado antes de ser usado
	private ArrayList <String > identifiers = new ArrayList<String>();;
	
//	utilizado para verificar se o identificador é do mesmo tipo que a expr	
	Map<String, TokenType> tpIdenti = new HashMap<>();
	
	private Token token, tokenAux;
	private int position;


	public Parser(List<Token> source) {
		this.source = source;
		this.token = null;
		this.position = 0;
	}
	

	public void parse() {
		getNextToken();
		while (this.token.tokentype != TokenType.End_of_input) {
			comandos();
		}
		System.out.println("Sem erros");
	}

	public Token getNextToken() {
		this.token = this.source.get(this.position);
		position++;
		return this.token;
	}

	public void comandos() {
		if (this.token.tokentype == TokenType.Reservado_imprimir) {
			getNextToken();
			expect("Print", TokenType.ParenEsquerdo);
			expr();
			expect("Print", TokenType.ParenDireito);
			expect("Print", TokenType.PontoVirgula);
		} else if (this.token.tokentype == TokenType.PontoVirgula) {
			getNextToken();
		} else if (this.token.tokentype == TokenType.Reservado_Inteiro || this.token.tokentype == TokenType.Reservado_Real) {
			TokenType aux;
			aux = (this.token.tokentype == TokenType.Reservado_Inteiro) ? TokenType.Reservado_Inteiro : TokenType.Reservado_Real;
			getNextToken();
			declarations.add(this.token);
			tokenAux = this.token;
			tpIdenti.put(tokenAux.value, aux);
			expect("identificador", TokenType.Identificador);
			assign(tpIdenti.get(tokenAux.value));
			identifiers.add(this.tokenAux.value);
		}  else if (this.token.tokentype == TokenType.Identificador) {
			if (identifiers.contains(this.token.value)) {
				tokenAux = this.token;
				getNextToken();
				assign(tpIdenti.get(tokenAux.value));
			} else {
				Erro(this.token.line, this.token.pos, "O identificador: " + this.token.value + " Precisa ser declarado antes de ser usado");
			}
			
		} else if (this.token.tokentype == TokenType.End_of_input) {
		} else {
			Erro(this.token.line, this.token.pos, "Erro de comando encontrado: " + this.token.tokentype);
		}
	}
	
	public void assign(TokenType tp) {
		expect("Atribuição", TokenType.Op_atribuir);
		exprAssign(tp);
		expect(";", TokenType.PontoVirgula);
	}

	public void expect(String msg, TokenType s) {
		if (this.token.tokentype == s) {
			getNextToken();
			return;
		}
		Erro(this.token.line, this.token.pos, msg + ": Esperando '" + s + "', encontrado: '" + this.token.tokentype + "'");
	}

	public void expr() {
		if (this.token.tokentype == TokenType.ParenEsquerdo) {
			paren_expr();
		} else if (this.token.tokentype == TokenType.Op_subtrair) {
			getNextToken();
			expr();
		} else if (this.token.tokentype == TokenType.Identificador) {
			if (identifiers.contains(this.token.value)) {
				getNextToken();
			} else {
				Erro(this.token.line, this.token.pos, "O identificador: " + this.token.value + " Precisa ser declarado antes de ser usado");
			}
		} else if (this.token.tokentype == TokenType.InteiroConst || this.token.tokentype == TokenType.RealConst) {
			getNextToken();
		} else {
			Erro(this.token.line, this.token.pos, "Esperando identificador ou numero, antes de: " + this.token.tokentype);
		}

		if (this.token.tokentype == TokenType.Op_somar || this.token.tokentype == TokenType.Op_subtrair
				|| this.token.tokentype == TokenType.Op_multiplicar || this.token.tokentype == TokenType.Op_dividir) {
			getNextToken();
			expr();
		}
	}
	
	public void exprAssign(TokenType tp) {
		if (this.token.tokentype == TokenType.ParenEsquerdo) {
			paren_exprAssign(tp);
		} else if (this.token.tokentype == TokenType.Op_subtrair) {
			getNextToken();
			exprAssign(tp);
		} else if (this.token.tokentype == TokenType.Identificador) {
			if (identifiers.contains(this.token.value)) {
				if (tpIdenti.get(this.token.value) == tp || (tp == TokenType.Reservado_Real && tpIdenti.get(this.token.value) == TokenType.Reservado_Inteiro)) {
					getNextToken();
				} else {
					Erro(this.token.line, this.token.pos, "O identificador: " + this.token.value + " não é do mesmo tipo, que o identificador de destino");
				}
			} else {
				Erro(this.token.line, this.token.pos, "O identificador: " + this.token.value + " Precisa ser declarado antes de ser usado");
			}
		} else if (this.token.tokentype == TokenType.InteiroConst) {
			getNextToken();
		} else if (this.token.tokentype == TokenType.RealConst) {
			if(tp == TokenType.Reservado_Real) {
				getNextToken();
			} else {
				Erro(this.token.line, this.token.pos, "A constante numerica: " + this.token.value + " não é do mesmo tipo, que o identificador de destino");
			}
		} else {
			Erro(this.token.line, this.token.pos, "Esperando identificador ou numero, antes de: " + this.token.tokentype);
		}

		if (this.token.tokentype == TokenType.Op_somar || this.token.tokentype == TokenType.Op_subtrair
				|| this.token.tokentype == TokenType.Op_multiplicar || this.token.tokentype == TokenType.Op_dividir) {
			getNextToken();
			exprAssign(tp);
		}
	}
	
	
	public void paren_expr() {
		expect("paren_expr", TokenType.ParenEsquerdo);
		expr();
		expect("paren_expr", TokenType.ParenDireito);
	}
	
	public void paren_exprAssign(TokenType tp) {
		expect("paren_expr", TokenType.ParenEsquerdo);
		exprAssign(tp);
		expect("paren_expr", TokenType.ParenDireito);
	}

	static void Erro(int line, int pos, String msg) {
		if (line > 0 && pos > 0) {
			System.out.printf("Erro: %s in line %d, pos %d\n", msg, line, pos);
		} else {
			System.out.println(msg);
		}
		System.exit(1);
	}
	
	public ArrayList<Token> getDeclarations() {
		return declarations;
	}

}