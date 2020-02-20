package algoritmo_ensamblador;

import java.io.*;

import java.text.Collator;

import java.util.StringTokenizer;


public class archivo {
	public void buscar() {
		try {
			File f = new File("P1ASM.txt");
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			Collator comparador = Collator.getInstance();
			comparador.setStrength(Collator.PRIMARY);
			if (f.exists()) {
				String linea, COMENTARIO = null, ETIQUETA = null, CODOP = null, OPERANDO = null;
				int cont_lin = 0, contador_puntos = 0;
				boolean error=false;
				while ((linea = br.readLine()) != null) {
					if (linea.matches("^[;].*")) {
						if (linea.length() <= 81) {
							COMENTARIO = linea;
							System.out.println("COMENTARIO: " + COMENTARIO);
						} else
							System.out.println("COMENTARIO MAYOR A 80 CARACTERES");
					} else {
						StringTokenizer st = new StringTokenizer(linea);
						if (st.countTokens() == 3 && linea.matches("^[a-zA-Z].*")) {
							ETIQUETA = st.nextToken();
							if (ETIQUETA.length() <= 8 && ETIQUETA.matches("\\w*|[_]*")) {	
								char[] etiquetarr = ETIQUETA.toCharArray();
								for (int i = 0; i < etiquetarr.length; i++) {
									if (etiquetarr[i]==ETIQUETA.charAt(0) && i!=0 ) {
										System.out.println("Error se repitio la letra en la etiqueta");
										error=true;
									}
								}
								if (error) {									
									break;
								}
							}else{
								System.out.println("Error Caracter no valido");
							} // ETiQUETA
							contador_puntos=0;
							error=false;
							CODOP = st.nextToken();
							if (CODOP.matches("^[a-zA-Z].*") && CODOP.length() <= 5) {
								char[] codopar = CODOP.toCharArray();
								for (int k = 0; k < CODOP.length(); k++) {
									if (codopar[k]=='.') {
										contador_puntos++;
									}							
								}
							}else{
								System.out.println("Error en la sintaxis de codop");
								break;
							}
								
							if (contador_puntos >1) {
								System.err.println("Hay mas de un punto en el ");
								break;
							}
							///////CODOP
							OPERANDO=st.nextToken();
							//OPERANDO
						}else if (st.countTokens() == 2 && linea.matches("^[a-zA-Z].*")) {  // dos toquens y un tabulador plox
							ETIQUETA = st.nextToken();
							if (ETIQUETA.length() <= 8 && ETIQUETA.matches("\\w*|[_]*")) {	
								char[] etiquetarr = ETIQUETA.toCharArray();
								for (int i = 0; i < etiquetarr.length; i++) {
									if (etiquetarr[i]==ETIQUETA.charAt(0) && i!=0 ) {
										System.out.println("Error se repitio la letra en la etiqueta");
										error=true;
									}
								}
								if (error) {									
									break;
								}
							}else{
								System.out.println("Error Caracter no valido");
							} // ETiQUETA
							
							contador_puntos=0;
							error=false;
							CODOP = st.nextToken();
							if (CODOP.matches("^[a-zA-Z].*") && CODOP.length() <= 5) {
								char[] codopar = CODOP.toCharArray();
								for (int k = 0; k < CODOP.length(); k++) {
									if (codopar[k]=='.') {
										contador_puntos++;
									}							
								}
							}
							if (contador_puntos >1) {
								System.err.println("Hay mas de un punto en el ");
								break;
							}
							
						} else if (linea.charAt(0)==' '|| linea.charAt(0)=='\t') {
							if (st.countTokens()==2) {
								contador_puntos=0;
								error=false;
								CODOP = st.nextToken();
								if (CODOP.matches("^[a-zA-Z].*") && CODOP.length() <= 5) {
									char[] codopar = CODOP.toCharArray();
									for (int k = 0; k < CODOP.length(); k++) {
										if (codopar[k]=='.') {
											contador_puntos++;
										}							
									}
								}
								if (contador_puntos >1) {
									System.err.println("Hay mas de un punto en el ");
									break;
								}
								///////CODOP
								OPERANDO=st.nextToken();
								//OPERANDO
							}else if (st.countTokens()==1) {
								contador_puntos=0;
								error=false;
								CODOP = st.nextToken();
								if (CODOP.matches("^[a-zA-Z].*") && CODOP.length() <= 5) {
									char[] codopar = CODOP.toCharArray();
									for (int k = 0; k < CODOP.length(); k++) {
										if (codopar[k]=='.') {
											contador_puntos++;
										}							
									}
								}else{
									System.out.println("Error en la sintaxis de codop");
									break;
								}
								if (contador_puntos >1) {
									System.err.println("Hay mas de un punto en el ");
									break;
								}
								///////CODOP
								
							}
						}
		
						
						System.out.println("ETIQUETA: "+ETIQUETA);
						System.out.println("CODOP: "+CODOP);
						System.out.println("OPERANDO: "+OPERANDO);
						if (comparador.equals(CODOP,"END")) {
							System.out.println("DETECTO END");							
							break;
						}
						
					}		
					COMENTARIO=null;
					ETIQUETA=null;
					CODOP=null;
					OPERANDO=null;
					
					System.out.println(++cont_lin+1+" ------------------------------------------------------------");				
				} // while readline txt
				br.close();
				fr.close();
			} else
				System.out.println("Error al abrir el archivo");
		} catch (Exception e) {
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		archivo a1 = new archivo();
		a1.buscar();

	}

}
