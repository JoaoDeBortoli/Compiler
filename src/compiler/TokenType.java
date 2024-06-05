package compiler;

public enum TokenType {
	Identificador,
	InteiroConst,
	RealConst,
	
	Reservado_imprimir,
	Reservado_Inteiro,
	Reservado_Real,
	
	Op_multiplicar,
	Op_dividir,
	Op_somar,
	Op_subtrair,
	Op_atribuir,
	
	ParenEsquerdo,
	ParenDireito,
	PontoVirgula,
	
	End_of_input
}