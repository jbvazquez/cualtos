using System;
namespace Perceptron2 {
	public class Program {
		public static void Main(String[] args){
 int[,] tabla = { { 1, 1, 1 }, { 1, 0, 0 }, { 0, 1, 0 }, { 0, 0, 0 } }; //Tabla de verdad AND: { x1, x2, salida }
 Random azar = new Random();
 double[] pesos = { azar.NextDouble(), azar.NextDouble(), azar.NextDouble() }; //Inicia los pesos al azar
 bool aprendiendo = true;
 int salidaEntera, iteracion = 0;
 double tasaAprende = 0.3;
 while (aprendiendo) { //Hasta que aprenda la tabla AND
 	iteracion++;
 	aprendiendo = false;
 	for (int cont = 0; cont <= 3; cont++) {
 double salidaReal = tabla[cont, 0] * pesos[0] + tabla[cont, 1] * pesos[1] + pesos[2]; //Calcula la salida real
 if (salidaReal > 0) salidaEntera = 1; 
 else salidaEntera = 0; //Transforma a valores 0 o 1
 int error = tabla[cont, 2] - salidaEntera;
 if (error != 0){ //Si la salida no coincide con lo esperado, cambia los pesos con la fórmula de Frank Rosenblatt
 	pesos[0] += tasaAprende * error * tabla[cont, 0];
 	pesos[1] += tasaAprende * error * tabla[cont, 1];
 	pesos[2] += tasaAprende * error * 1;
 aprendiendo = true; //Y sigue buscando
}
}
}

Console.WriteLine("Iteraciones: " + iteracion.ToString());
Console.WriteLine("Peso 1: " + pesos[0].ToString());
Console.WriteLine("Peso 2: " + pesos[1].ToString());
Console.WriteLine("Peso 3: " + pesos[2].ToString());
 for (int cont = 0; cont <= 3; cont++){ //Muestra el perceptron con la tabla AND aprendida
 	double salidaReal = tabla[cont, 0] * pesos[0] + tabla[cont, 1] * pesos[1] + pesos[2];
 	if (salidaReal > 0) salidaEntera = 1; 
 	else salidaEntera = 0;
 	Console.WriteLine(tabla[cont, 0] + " y " + tabla[cont, 1] + " = " + tabla[cont, 2] + " perceptron: " +
 		salidaEntera);
 }
 Console.ReadKey();
}
}
}
