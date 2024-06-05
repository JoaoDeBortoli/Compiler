package compiler;

import java.util.List;

public class ChangeJava {
	
	List<Token> source;
	
	public ChangeJava(List<Token> source){
		this.source = source;
	}
	
	public String change() {
		String prog = "";
		String palavra;
		char chr;
		for(Token lista : source) {
			if(lista.tokentype == TokenType.PontoVirgula) {
				prog += lista.value + "\n\t\t";
			} else if(lista.tokentype == TokenType.Reservado_Inteiro) {
				prog += "int ";
			} else if(lista.tokentype == TokenType.Reservado_Real) {
				prog += "double ";
			} else if(lista.tokentype == TokenType.Reservado_imprimir) {
				prog += "System.out.println";
			} else if(lista.tokentype == TokenType.Identificador) {
				palavra = "";
				for (int i = 0; i < lista.value.length(); i++) {
					chr = lista.value.charAt(i);
					if (chr == 'ç') {
						palavra += "c";
					} else if(chr == 'Ç') {
						palavra += "c";
					} else if("áãâà".indexOf(chr) != -1) {
						palavra += "a";
					} else if("éêè".indexOf(chr) != -1) {
						palavra += "e";
					} else if("íîì".indexOf(chr) != -1) {
						palavra += "i";
					} else if("óôòõ".indexOf(chr) != -1) {
						palavra += "o";
					} else if("úûù".indexOf(chr) != -1) {
						palavra += "u";
					} else if("ÁÃÂÀ".indexOf(chr) != -1) {
						palavra += "A";
					} else if("ÉÊÈ".indexOf(chr) != -1) {
						palavra += "E";
					} else if("ÍÎÌ".indexOf(chr) != -1) {
						palavra += "I";
					} else if("ÓÔÒÕ".indexOf(chr) != -1) {
						palavra += "O";
					} else if("ÚÛÙ".indexOf(chr) != -1) {
						palavra += "U";
					} else {
						palavra += chr;
					}
				}
				prog += palavra;
			} else {
				prog += lista.value;
			}
		}
		String conteudo = "package programa;\npublic class Programa {\n\tpublic static void main(String[] args) {\n\n\t\t" +
				prog + "\n\t}\n}";
		return conteudo;
	}
}