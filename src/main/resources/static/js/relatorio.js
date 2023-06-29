function alterarCorGrafico(valor) {

	let cor = "green";

	if (valor == 100.00999999999999)
		cor = "blue"
	else if (valor == 100.02000000000001)
		cor = "dimgray"
	else if (valor == 100.03)
		cor = "#fce903"
	else if (valor < 80)
		cor = "red"
	
//	console.log(valor +  " " +cor);

	return cor;
}

function arrendondarValorGrafico(valor) {
	var not = valor;
	not = (valor.toFixed(1));
	return not;
}

function arrendondarValorTabela(valor) {
	return valor.toFixed(2);
}