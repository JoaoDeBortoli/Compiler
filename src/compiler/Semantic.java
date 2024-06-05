package compiler;

import java.util.ArrayList;
import java.util.List;

public class Semantic {
	private Parser parser;
	private ArrayList<Token> declarations;
	
	public Semantic(List<Token> source) {
		parser = new Parser(source);
		parser.parse();
	}
	
	public void analyzeProgram() {
		declarations = parser.getDeclarations();
		checkDeclarations();	
	}
	
	private void checkDeclarations(){
		String name1 = "", name2 =  "";
		for(int i = 0; i < declarations.size(); i++){
			name1 = declarations.get(i).value; 
			for(int j = i + 1; j < declarations.size(); j ++){
				name2 = declarations.get(j).value; 
				
				if(name1.equals(name2)) {
					Erro(declarations.get(j).line, declarations.get(j).pos, "Identificador " +name2 + " declarado multiplas vezes");
				}
			}
		}
	}
	
	static void Erro(int line, int pos, String msg) {
		if (line > 0 && pos > 0) {
			System.out.printf("Erro: %s in line %d, pos %d\n", msg, line, pos);
		} else {
			System.out.println(msg);
		}
		System.exit(1);
	}

}