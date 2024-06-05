package programa;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import compiler.*;

public class Main {
	public static void main(String[] args) {
		try {
			Token t;
			List<Token> list = new ArrayList<>();
			File f = new File("src/programa/Programa.txt");
			Scanner s = new Scanner(f);
			String source = " ";
			while (s.hasNext()) {
				source += s.nextLine() + "\n";
			}
			Lexer l = new Lexer(source);			
			while ((t = l.getToken()).tokentype != TokenType.End_of_input) {
				list.add(new Token(t.tokentype, t.value, t.line, t.pos));
			}
			list.add(new Token(t.tokentype, t.value, t.line, t.pos));
			for(Token lista : list) {
				System.out.println(lista);
			}
			
			Semantic sc = new Semantic(list);
			sc.analyzeProgram();
			ChangeJava c = new ChangeJava(list);
			String conteudo = c.change();
			
			FileWriter arquivo = new FileWriter("src/programa/Programa.java");
			arquivo.write(conteudo);
			arquivo.close();
	
			s.close();
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
		}
	}
}
