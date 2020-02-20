import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;


public class P02_STL {
	public static int CONTLOC = 0;
	public static String OPERANDO_DEC;//VARIABLE GLOBAL QUE GUARDA LA EL OPERANDO EN HEXADECIMAL

	public void codigo_maquina() {
		try {
			P02_STL funcion = new P02_STL();
			File temp = new File("P1tem.txt");
			FileReader fr = new FileReader(temp);
			BufferedReader br = new BufferedReader(fr);
			if (temp.exists()) {
				System.out.println("\n"+temp.getName());
				System.out.println("_*****************************************************************_");
				String linea;
				String VALOR_TEMP = null, ETIQUETA_TEMP = null, CODOP_TEMP = null, OPERANDO_TEMP = null;
				int codm=0;
				while ((linea = br.readLine()) != null) {
					// System.out.println(linea);
					StringTokenizer st = new StringTokenizer(linea);
					if (st.countTokens() == 5) {
						st.nextToken();
						VALOR_TEMP = st.nextToken();
						ETIQUETA_TEMP = st.nextToken();
						CODOP_TEMP = st.nextToken();
						OPERANDO_TEMP = st.nextToken();
						// System.out.println(st.nextToken()+"\t"+st.nextToken()+"\t"+st.nextToken()+"\t"+st.nextToken()+"\t"+st.nextToken());
						// System.out.println("VALOR: "+VALOR_TEMP+" ETIQUETA:
						// "+ETIQUETA_TEMP+" CODOP: "+CODOP_TEMP+" OPERANDO:
						// "+OPERANDO_TEMP);
						System.out.println("VALOR_TEMP: "+VALOR_TEMP+" CODOP_TEMP: " + CODOP_TEMP + " OPERANDO_TEMP: " + OPERANDO_TEMP);
						if (OPERANDO_TEMP.equals("null") && OPERANDO_TEMP.length() == 4) { // eliminar
																							// el contenido de la variable
																							// string
																							// obtenida
																							// porque
																							// sino
																							// se
																							// confundira en el metodo de busqueda tabop
																							// con
																							// un
																							// string
																							// aunque
																							// ese
																							// string
																							// sea
																							// "null"
																							// no
																							// es
																							// igual
																							// a
																							// null
							OPERANDO_TEMP = null;
						}
						if (OPERANDO_TEMP!= null ) {//para comprobar que pueda convertir algo
							 codm=funcion.base_numerica(OPERANDO_TEMP);//convertir de cualquier base a decimal
							 OPERANDO_DEC= Integer.toHexString(codm);//convertir de decimal a hexa u guardarlo en variable global
						}
						funcion.buscar_codop(CODOP_TEMP, OPERANDO_TEMP);		
					
						VALOR_TEMP = null;
						ETIQUETA_TEMP = null;
						CODOP_TEMP = null;
						OPERANDO_TEMP = null;
						
						
						System.out.println("_*****************************************************************_");
					}

				}

				br.close();
				fr.close();

			} else {
				System.out.println("Error al abrir el archivo temporal");
			}

		} catch (Exception e) {
		}

	}

	public int base_numerica(String OPERANDOS) { // OBTENER EL VALOR EN
		// DECIMAL DE CADA BASE
		// NUMERICA
		String valor = "";
		int val = 0;
		if (OPERANDOS.matches("^\\^#+$+[0-9A-Fa-f]+$") || OPERANDOS.matches("^#+@+[0-7]")
				|| OPERANDOS.matches("^#+%+[0-1]+$") || OPERANDOS.matches("^#+[0-9]+$")) {
			OPERANDOS = OPERANDOS.replaceAll("#", "");
			// System.out.println("OPERANDO CON #: "+OPERANDOS);
		}
		if (OPERANDOS.matches("^\\$+[0-9A-Fa-f]+$")) {
			OPERANDOS = OPERANDOS.replaceAll("\\$|@|%", "");
			int hex = Integer.parseInt(OPERANDOS, 16);
			valor = Integer.toString(hex);
		} else if (OPERANDOS.matches("^@+[0-7]+$")) {
			OPERANDOS = OPERANDOS.replaceAll("\\$|@|%", "");
			int octal = Integer.parseInt(OPERANDOS, 8);
			valor = Integer.toString(octal);
		} else if (OPERANDOS.matches("%+[0-1]+$")) {
			OPERANDOS = OPERANDOS.replaceAll("\\$|@|%", "");
			int bin = Integer.parseInt(OPERANDOS, 2);
			valor = Integer.toString(bin);
		} else if (OPERANDOS.matches("^[0-9]+$")) {
			OPERANDOS = OPERANDOS.replaceAll("\\$|@|%", "");
			int dec = Integer.parseInt(OPERANDOS);
			valor = Integer.toString(dec);
		}
		System.out.println("CONVERSION: " + valor);
		val = Integer.parseInt(valor);
		return val;
	}

	public String valdirectiva(String ETIQUETA, String CODOP, String OPERANDOS) {// DIRECTIVAS
																					// DE
		// LENGUAJE
		// ENSAMBLADOR
		String result = "";
		int valor = 0;
		Collator comparador = Collator.getInstance();
		comparador.setStrength(Collator.PRIMARY);
		P02_STL funcion = new P02_STL();
		switch (CODOP) {
		case "ORG":
			result = "DIR_INIC ";
			if (OPERANDOS.matches("^\\$+[0-9A-Fa-f]+$")) {
				OPERANDOS = OPERANDOS.replaceAll("\\$|@|%", "");
				int hex = Integer.parseInt(OPERANDOS, 16);
				if (hex >= 0 && hex <= 65535) {
					CONTLOC = hex;
					result = result + "HEXA " + CONTLOC;
				}
			} else if (OPERANDOS.matches("^@+[0-7]+$")) {
				OPERANDOS = OPERANDOS.replaceAll("\\$|@|%", "");
				int octal = Integer.parseInt(OPERANDOS, 8);
				if (octal >= 0 && octal <= 65535) {
					CONTLOC = octal;
					result = result + "OCTAL " + CONTLOC;
				}
			} else if (OPERANDOS.matches("%+[0-1]+$")) {
				OPERANDOS = OPERANDOS.replaceAll("\\$|@|%", "");
				int bin = Integer.parseInt(OPERANDOS, 2);
				if (bin >= 0 && bin <= 65535) {
					CONTLOC = bin;
					result = result + "BINARIO " + CONTLOC;
				}
			} else if (OPERANDOS.matches("^[0-9]+$")) {
				OPERANDOS = OPERANDOS.replaceAll("\\$|@|%", "");
				int dec = Integer.parseInt(OPERANDOS);
				if (dec >= 0 && dec <= 65535) {
					CONTLOC = dec;
					result = result + "DECIMAL " + CONTLOC;
				}
			}
			break;
		case "EQU":
			valor = funcion.base_numerica(OPERANDOS);
			CONTLOC++;
			result = "EQUATE" + " ETIQUETA: " + ETIQUETA + " " + valor;
			break;
		case "END":
			result = "END";
			break;
		default:
			result = null;
			break;
		}
		if (CODOP.matches("DW|DB|DC.W|DC.B|FCB|FDB|FCC+$")) { // DIRECTIVAS DE
																// // CONSTANTES
			switch (CODOP) {
			case "DB":// 1 BYTE
				valor = funcion.base_numerica(OPERANDOS);
				if (valor >= 0 && valor <= 255) {
					CONTLOC++;
					result = "DEFINE BYTE";
				} else {
					result = "DEFINE BYTE SOLAMENTE PUEDE TENER VALORES DE ENTRE 0-255 DEC";
				}
				break;
			case "DC.B":// 1 BYTE
				valor = funcion.base_numerica(OPERANDOS);
				if (valor >= 0 && valor <= 255) {
					CONTLOC++;
					result = "DEFINE CONSTANT.BYTE";
				} else {
					result = "DEFINE CONSTANT.BYTE SOLAMENTE PUEDE TENER VALORES DE ENTRE 0-255 DEC";
				}
				break;
			case "FCB":// 1 BYTE
				valor = funcion.base_numerica(OPERANDOS);
				if (valor >= 0 && valor <= 255) {
					CONTLOC++;
					result = "FULL CONSTANT BYTE";
				} else {
					result = "FULL CONSTANT BYTE SOLAMENTE PUEDE TENER VALORES DE ENTRE 0-255 DEC";
				}
				break;
			case "DW":// 2 BYTE
				valor = funcion.base_numerica(OPERANDOS);
				if (valor >= 0 && valor <= 65535) {
					result = "DEFINE WORD";
					CONTLOC = CONTLOC + 2;
				} else {
					result = "DEFINE WORD SOLAMENTE PUEDE TENER VALORES DE ENTRE 0-6535 DEC";
				}
				break;
			case "DC.W":// 2 BYTE
				valor = funcion.base_numerica(OPERANDOS);
				if (valor >= 0 && valor <= 65535) {
					CONTLOC = CONTLOC + 2;
					result = "DEFINE CONSTANT WORD";
				} else {
					result = "DEFINE CONSTANT WORD SOLAMENTE PUEDE TENER VALORES DE ENTRE 0-6535 DEC";
				}
				break;
			case "FDB":// 2 BYTE
				valor = funcion.base_numerica(OPERANDOS);
				if (valor >= 0 && valor <= 65535) {
					CONTLOC = CONTLOC + 2;
					result = "FULL DOUBLE BYTE";

				} else {
					result = "FULL DOUBLE BYTE SOLAMENTE PUEDE TENER VALORES DE ENTRE 0-6535 DEC";
				}
				break;
			case "FCC":// DE CARACTERES ASCII
				// if (OPERANDOS.matches("^\"+.*|\\s*+\"")) {
				if (OPERANDOS.startsWith("\"") && OPERANDOS.endsWith("\"")) {
					OPERANDOS = OPERANDOS.replaceAll("\"", "");
					CONTLOC = CONTLOC + OPERANDOS.length();
					result = OPERANDOS;
				}
				break;
			default:
				break;
			}// switch
		} else if (CODOP.matches("DS|DS.B|DS.W|RMB|RMW+$")) {// DIRECTIVAS DE
			// ESPACIO DE
			// MEMORIA
			switch (CODOP) {
			case "DS": // 1 BYTE
				valor = funcion.base_numerica(OPERANDOS);
				if (valor >= 0 && valor <= 65535) {
					CONTLOC = CONTLOC + (valor * 1);
					result = "DEFINE SPACE";
				} else {
					result = "DEFINE SPACE SOLAMENTE PUEDE TENER VALORES DE ENTRE 0-6535 DEC";
				}
				break;
			case "DS.B": // 1 BYTE
				valor = funcion.base_numerica(OPERANDOS);
				if (valor >= 0 && valor <= 65535) {
					CONTLOC = CONTLOC + (valor * 1);
					result = "DEFINE SPACE.BYTE";
				} else {
					result = "DEFINE SPACE.BYTE SOLAMENTE PUEDE TENER VALORES DE ENTRE 0-6535 DEC";
				}
				break;
			case "RMB": // 1 BYTE
				valor = funcion.base_numerica(OPERANDOS);
				if (valor >= 0 && valor <= 65535) {
					CONTLOC = CONTLOC + (valor * 1);
					result = "RESERVE MEMORY BYTE";
				} else {
					result = "RESERVE MEMORY BYTE SOLAMENTE PUEDE TENER VALORES DE ENTRE 0-6535 DEC";
				}
				break;
			case "DS.W": // 2 B EN 2 B
				valor = funcion.base_numerica(OPERANDOS);
				if (valor >= 0 && valor <= 65535) {
					CONTLOC = CONTLOC + (valor * 2);
					result = "DEFINE SPACE.WORD";
				} else {
					result = "DEFINE SPACE.WORD SOLAMENTE PUEDE TENER VALORES DE ENTRE 0-6535 DEC";
				}
				break;
			case "RMW": // 2 B EN 2 B
				valor = funcion.base_numerica(OPERANDOS);
				if (valor >= 0 && valor <= 65535) {
					CONTLOC = CONTLOC + (valor * 2);
					result = "RESERVE MEMORY WORD";
				} else {
					result = "RESERVE MEMORY WORD SOLAMENTE PUEDE TENER VALORES DE ENTRE 0-6535 DEC";
				}
				break;
			default:
				break;
			}
		}
		return result;

	}

	public boolean valetiqueta(String ETIQUETA) {
		boolean etq = true;
		if (ETIQUETA.length() <= 8 && Character.isLetter(ETIQUETA.charAt(0))) {
			char[] etiquetar = ETIQUETA.toCharArray();
			for (int i = 0; i < etiquetar.length; i++) {
				if (!Character.isLetterOrDigit(etiquetar[i]) && etiquetar[i] != 95) {
					etq = false;
				}
			}
		} else {
			etq = false;
		}
		if (etq == false) {
			System.out.println("ERROR EN SINTAXIS ETIQUETA");
		}

		return etq;
	}

	public void modo_de_direccionamiento(ArrayList<String> tabop, String OPERANDOS, String CODOPS, String MODIRS) {
		// if (OPERANDOS != null) {
		// System.out.println("OPERANDO RECIBIDO:\t" + OPERANDOS);
		// }
		boolean INHERENTE = false;
		String CODOP_FINAL = "", MODIR_FINAL = "";
		P02_STL funcion = new P02_STL();
		System.out.println("COINICDENCIAS TABOP: " + tabop.size());
		Iterator<String> nombreIterator = tabop.iterator();
		Collator comparadors = Collator.getInstance();
		comparadors.setStrength(Collator.PRIMARY);
		while (nombreIterator.hasNext()) {
			String elemento = nombreIterator.next();
			// System.out.println(elemento);
			StringTokenizer st = new StringTokenizer(elemento);
			st.nextToken();
			if (comparadors.equals(st.nextToken(), "FALSE") && OPERANDOS == null) {
				// if (st.nextToken().equals("FALSE")&& OPERANDOS == null) {
				System.out.println("\tINHERENTE");
				CODOP_FINAL = CODOPS;
				MODIR_FINAL = "INH";
				INHERENTE = true;
			} else if (elemento.contains("FALSE") && OPERANDOS != null) {
				System.out.println("ERROR NO DEBE DE TENER OPERANDO");
				return;
			}
		}
		if (INHERENTE) {

		} else if (OPERANDOS.matches("^#+\\$+[0-9A-Fa-f]+$")) {
			System.out.println("\tINMEDIATO HEXA");
			CODOP_FINAL = CODOPS;
			MODIR_FINAL = "INM";
		} else if (OPERANDOS.matches("^#+@+[0-7]+$")) {
			System.out.println("\tINMEDIATO OCTAL");
			CODOP_FINAL = CODOPS;
			MODIR_FINAL = "INM";
		} else if (OPERANDOS.matches("^#+%+[0-1]+$")) {
			System.out.println("\tINMEDIATO BINARIO");
			CODOP_FINAL = CODOPS;
			MODIR_FINAL = "INM";
		} else if (OPERANDOS.matches("^#+[0-9]+$")) {
			System.out.println("\tINMEDIATO DECIMAL");
			CODOP_FINAL = CODOPS;
			MODIR_FINAL = "INM";
		} else if (OPERANDOS.matches("^\\$+[0-9A-Fa-f]+$")) {
			System.out.print("\tHEXA");
			OPERANDOS = OPERANDOS.replaceAll("\\$|@|%", "");
			int hex = Integer.parseInt(OPERANDOS, 16);
			if (hex >= 0 && hex <= 255) {
				System.out.println("\tDIRECTO");
				CODOP_FINAL = CODOPS;
				MODIR_FINAL = "DIR";
			} else if (hex >= 255 && hex <= 65535) {
				System.out.println("\tEXTENDIDO");
				CODOP_FINAL = CODOPS;
				MODIR_FINAL = "EXT";
			} else {
				System.out.println("\tDESBORDAMIENTO");
			}
		} else if (OPERANDOS.matches("^@+[0-7]+$")) {
			System.out.print("\tOCTAL");
			OPERANDOS = OPERANDOS.replaceAll("\\$|@|%", "");
			int octal = Integer.parseInt(OPERANDOS, 8);
			if (octal >= 0 && octal <= 255) {
				System.out.println("\tDIRECTO");
				CODOP_FINAL = CODOPS;
				MODIR_FINAL = "DIR";
			} else if (octal >= 255 && octal <= 65535) {
				System.out.println("\tEXTENDIDO");
			} else {
				System.out.println("\tDESBORDAMIENTO");
			}
		} else if (OPERANDOS.matches("%+[0-1]+$")) {
			System.out.print("\tBINARIO");
			OPERANDOS = OPERANDOS.replaceAll("\\$|@|%", "");
			int bin = Integer.parseInt(OPERANDOS, 2);
			if (bin >= 0 && bin <= 255) {
				System.out.println("\tDIRECTO");
				CODOP_FINAL = CODOPS;
				MODIR_FINAL = "DIR";
			} else if (bin >= 255 && bin <= 65535) {
				System.out.println("\tEXTENDIDO");
				CODOP_FINAL = CODOPS;
				MODIR_FINAL = "EXT";
			} else {
				System.out.println("\tDESBORDAMIENTO");
			}
		} else if (OPERANDOS.matches("^[0-9]+$")) {
			System.out.print("\tDECIMAL");
			OPERANDOS = OPERANDOS.replaceAll("\\$|@|%", "");
			int dec = Integer.parseInt(OPERANDOS);
			if (dec >= 0 && dec <= 255) {
				System.out.println("\tDIRECTO");
				CODOP_FINAL = CODOPS;
				MODIR_FINAL = "DIR";
			} else if (dec >= 255 && dec <= 65535) {
				System.out.println("\tEXTENDIDO");
				CODOP_FINAL = CODOPS;
				MODIR_FINAL = "EXT";
			} else {
				System.out.println("tDESBORDAMIENTO");
			}
		} else if (OPERANDOS.matches("^,+[X|Y|SP|PC]+$") || OPERANDOS.matches("^,+[x|y|sp|pc]+$")) {
			OPERANDOS = 0 + OPERANDOS;
			System.out.println(OPERANDOS);

		} else if (OPERANDOS.matches("\\d+,+[x|X|y|Y|sp|SP|pc|PC]+$")) {
			String[] parts = OPERANDOS.split("(,)");
			String part1 = parts[0]; //
			// String part2 = parts[1]; //
			int aux = Integer.parseInt(part1);
			// System.out.println(aux);
			if (aux >= -16 && aux <= 15) {
				System.out.println("INDIZADO 5 BITS");
				CODOP_FINAL = CODOPS;
				MODIR_FINAL = "IDX";
			} else if (aux >= -256 && aux <= -17 || aux >= 16 && aux <= 255) {
				System.out.println("INDIZADO DE 9 BITS");
				CODOP_FINAL = CODOPS;
				MODIR_FINAL = "IDX1";
			} else if (aux >= 256 && aux <= 65535) {
				System.out.println("INDIZADO 16 BITS");
				CODOP_FINAL = CODOPS;
				MODIR_FINAL = "IDX2";
			}

		} else if (OPERANDOS.matches("^\\[+\\d+,+[x|X|y|Y|sp|SP|pc|PC]+\\]+$")) {
			OPERANDOS = OPERANDOS.replaceAll("\\[|\\]", "");
			// System.out.println(OPERANDOS);
			String[] parts = OPERANDOS.split("(,)");
			String part1 = parts[0]; //
			// String part2 = parts[1]; //
			int aux = Integer.parseInt(part1);
			if (aux >= 0 && aux <= 65535) {
				System.out.println("INDIZADO INDIRECTO 16 BITS");
				CODOP_FINAL = CODOPS;
				MODIR_FINAL = "[IDX2]";

			}
		} else if (OPERANDOS.matches("^[0-9]+,+-+[x|X|y|Y|sp|SP|pc|PC]+$")) {
			System.out.println("Indizado de pre decremento");
			CODOP_FINAL = CODOPS;
			MODIR_FINAL = "IDX";
		} else if (OPERANDOS.matches("^[0-9]+,+[\\+]+[x|X|y|Y|sp|SP|pc|PC]+$")) {
			System.out.println("Indizado de pre incremento");
			CODOP_FINAL = CODOPS;
			MODIR_FINAL = "IDX";
		} else if (OPERANDOS.matches("^[0-9]+,+[x|X|y|Y|sp|SP|pc|PC]+-")) {
			System.out.println("Indizado de auto post decremento");
			CODOP_FINAL = CODOPS;
			MODIR_FINAL = "IDX";
		} else if (OPERANDOS.matches("^[0-9]+,+[x|X|y|Y|sp|SP|pc|PC]+[\\+]+$")) {
			System.out.println("Indizado de auto post incremento");
			CODOP_FINAL = CODOPS;
			MODIR_FINAL = "IDX";
		} else if (OPERANDOS.matches("^[a|A|b|B|d|D]+,+[x|X|y|Y|sp|SP|pc|PC]")) {
			String[] parts = OPERANDOS.split("(,)");
			// String part1 = parts[0]; //
			String part2 = parts[1]; //
			System.out.println("INDIZADO DE ACUMULADOR" + " " + part2);
			CODOP_FINAL = CODOPS;
			MODIR_FINAL = "IDX";
		} else if (OPERANDOS.matches("^\\[+[d|D]+,+[x|X|y|Y|sp|SP|pc|PC]+\\]+$")) {
			System.out.println("INDIZADO INDIRECTO DE 16 BITS");
			CODOP_FINAL = CODOPS;
			MODIR_FINAL = "[D,IDX]";

		} else if (funcion.valetiqueta(OPERANDOS) && CODOPS.startsWith("L")) {
			System.out.println("Relativo 16 bits");
			CODOP_FINAL = CODOPS;
			MODIR_FINAL = "REL";
		} else if (funcion.valetiqueta(OPERANDOS) && CODOPS.startsWith("B")) {
			System.out.println("Relativo 8 bits");
			CODOP_FINAL = CODOPS;
			MODIR_FINAL = "REL";
		} else {
			System.out.println("No entro a ningun modo de direccionamiento");
		}
		// analizar el operando a cual corresponde y luego comparar con los
		// posibles modos de direccionamiento de dicho codop
		Iterator<String> Iterator = tabop.iterator();
		String CODOPA = "", MODIRA = "", TOTALB = "", CODMAQ = "";
		Collator comparador = Collator.getInstance();
		comparador.setStrength(Collator.PRIMARY);
		while (Iterator.hasNext()) {
			String elemento = Iterator.next();
			StringTokenizer st = new StringTokenizer(elemento);
			CODOPA = st.nextToken();
			st.nextToken();
			MODIRA = st.nextToken();
			CODMAQ = st.nextToken();
			st.nextToken();
			st.nextToken();
			TOTALB = st.nextToken();
			if (comparador.equals(CODOP_FINAL, CODOPA) && comparador.equals(MODIR_FINAL, MODIRA)) {
				System.out.println("TOTAL DE BYTES: " + TOTALB);
			//	System.out.println("CODIGO MAQUINA: " + CODMAQ);
				CONTLOC = CONTLOC + Integer.parseInt(TOTALB);
				break;
			}
		}
		System.out.println("CODOPF: " + CODOP_FINAL + " MODIRF: " + MODIR_FINAL);
		if (MODIR_FINAL.equals("INH")) {
			System.out.println("CODIGO MAQUINA: " + CODMAQ);
		}else if (MODIR_FINAL.equals("DIR")) {
			System.out.println("CODIGO MAQUINA: " + CODMAQ+OPERANDO_DEC);
			
		}

	}

	boolean codop_correcto(String CODOP) {
		boolean correcto = false;
		int contador_puntos = 0;
		if (CODOP.matches("^[a-zA-Z].*") && CODOP.length() <= 5) {
			char[] codopar = CODOP.toCharArray();
			for (int k = 0; k < CODOP.length(); k++) {
				if (codopar[k] == '.') {
					contador_puntos++;
				}
			}
		}
		if (contador_puntos > 1) {
			System.err.println("Hay mas de un punto en el ");
			correcto = false;
		} else {
			correcto = true;
		}
		return correcto;
	}

	public void buscar_codop(String CODOPS, String OPERANDOS) {
		// System.out.println("RECIBI: "+CODOPS+" "+OPERANDOS);
		ArrayList<String> tabop = new ArrayList<String>();
		boolean encontrado = false;
		try {
			String MODIRS = "";
			P02_STL funcion = new P02_STL();
			File f2 = new File("TABOP.txt");
			BufferedReader br2 = new BufferedReader(new FileReader(f2));
			Collator comparador = Collator.getInstance();
			comparador.setStrength(Collator.PRIMARY);
			if (f2.exists()) {
				// int cont_lin = 0;
				// String c="ADCA";
				String linea, CODOP = null, VB = null;
				String si = "TRUE";
				while ((linea = br2.readLine()) != null) {
					StringTokenizer st = new StringTokenizer(linea);
					if (st.countTokens() == 7) {
						CODOP = st.nextToken();
						funcion.codop_correcto(CODOP);
						if (funcion.codop_correcto(CODOP)) {
							// System.out.println("CODOP CORRECTO");
							VB = st.nextToken();
							if (VB.equals(si)) {
								if (comparador.equals(CODOPS, CODOP)) {
									MODIRS = st.nextToken();
									tabop.add(linea);
									encontrado = true;
								}
							} else {
								if (comparador.equals(CODOPS, CODOP)) {
									MODIRS = st.nextToken();
									tabop.add(linea);
									encontrado = true;
								}
							}
						} else {
							System.out.println("ERROR LA SINTAXIS DEL CODOP EN EL TABOP");
							break;
						}

					} else {
						System.out.println("ERROR EN TABOP");
						break;
					}
					VB = null;
					MODIRS = null;

					// System.out.println(++cont_lin+1+"TABOP------------------------------
					// ---------------- ---------");
				} // while
				if (encontrado) {
					funcion.modo_de_direccionamiento(tabop, OPERANDOS, CODOPS, MODIRS);
				} else {
					System.out.println("EL CODOP NO FUE ENCONTRADO EN TABOP");
				}
				tabop.clear();
				br2.close();
				// debe regresar algo
			} else {
				System.out.println("Error al abrir el archivo");
			}
		} catch (Exception e) {
		}
	}

	public void buscar() {
		try {
			File f = new File("P5ASM.txt");
			File temp1 = new File("P1tem.txt");
			File temp2 = new File("TABSIM.txt");
			if (temp1.exists() && temp2.exists() || temp1.exists() || temp2.exists()) {
				temp1.delete();
				temp2.delete();
			}
			Collator comparador = Collator.getInstance();
			comparador.setStrength(Collator.PRIMARY);
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			P02_STL funcion = new P02_STL();
			int contorg = 0;
			if (f.exists()) {
				String linea, COMENTARIO = null, ETIQUETA = null, CODOP = null, OPERANDO = null;
				int cont_lin = 0, contador_puntos = 0;
				int lineac = 0;
				boolean error = false;
				File f1 = new File("P1tem.txt");
				BufferedWriter bwi = new BufferedWriter(new FileWriter(f1, true));
				File t = new File("TABSIM.txt");
				BufferedWriter bwb = new BufferedWriter(new FileWriter(t, true));
				if (f1.exists() && t.exists()) {
					f1.delete();
					bwi.write("\t\t" + "VALOR" + "\t\t" + "ETIQUETA" + "\t" + "CODOP" + "\t" + "     OPERANDO");
					bwi.newLine();
					bwi.close();
					t.delete();
					bwb.write("Si" + "\t\t" + "Ti");
					bwb.newLine();
					bwb.close();

				}
				if (!f1.exists() && !t.exists()) {

					f1.createNewFile();
					bwi.write("h");
					bwi.write("\t\t" + "VALOR" + "\t\t" + "ETIQUETA" + "\t" + "CODOP" + "\t" + "     OPERANDO");
					bwi.newLine();
					t.createNewFile();
					bwb.write("Si" + "\t\t" + "Ti");
					bwb.newLine();
					bwb.close();
				}
				bwi.close();
				// f1.deleteOnExit();

				while ((linea = br.readLine()) != null) {
					lineac++;
					// COMENTARIO
					if (linea.matches("^[;].*")) {
						if (linea.length() <= 81) {
							COMENTARIO = linea;
							System.out.println("COMENTARIO: " + COMENTARIO);
						} else {
							System.out.println("COMENTARIO EN LA LINEA" + lineac + "MAYOR A 80 CARACTERES");
						}
					} else {
						StringTokenizer st = new StringTokenizer(linea); // \t\r\n
						// ETIQUETA CODOOP OPERANDO
						// ETIQUETA
						if (st.countTokens() == 3 && linea.matches("^[a-zA-Z].*")) {
							ETIQUETA = st.nextToken();
							if (ETIQUETA.length() <= 8 && ETIQUETA.matches("\\w*|[_]*")) {
								char[] etiquetarr = ETIQUETA.toCharArray();
								for (int i = 0; i < etiquetarr.length; i++) {
									if (etiquetarr[i] == ETIQUETA.charAt(0) && i != 0) {
										System.out.println(
												"Error se repitio la letra en la etiqueta de la linea " + lineac);
										error = true;
									}
								}
								if (error) {
									break;
								}
							} else {
								System.out.println("Error Caracter no valido");
							}

							// FIN ETiQUETA
							// CODOP
							contador_puntos = 0;
							error = false;
							CODOP = st.nextToken();
							if (CODOP.matches("^[a-zA-Z].*") && CODOP.length() <= 5) {
								char[] codopar = CODOP.toCharArray();

								for (int k = 0; k < CODOP.length(); k++) {
									if (codopar[k] == '.') {
										contador_puntos++;
									}
								}
							} else {
								System.out.println("Error en la sintaxis de codop de la linea " + lineac);
								break;
							}

							if (contador_puntos > 1) {
								System.err.println("Hay mas de un punto en el codop de la linea " + lineac);
								break;
							}
							// FIN CODOP
							// OPERANDO

							OPERANDO = st.nextToken();

							// OPERANDO
						} else if (st.countTokens() == 2 && linea.matches("^[a-zA-Z].*")) {

							// dos toquens y un tabulador plox
							// ETIQUETA Y CODOP
							ETIQUETA = st.nextToken();
							if (ETIQUETA.length() <= 8 && ETIQUETA.matches("\\w*|[_]*")) {
								char[] etiquetarr = ETIQUETA.toCharArray();
								for (int i = 0; i < etiquetarr.length; i++) {
									if (etiquetarr[i] == ETIQUETA.charAt(0) && i != 0) {
										System.out.println("Error se repitio la letra en la etiqueta");
										error = true;
									}
								}
								if (error) {
									break;
								}
							} else {
								System.out.println("Error Caracter no valido");
							}

							// FIN ETIQUETA
							// CODOP
							contador_puntos = 0;
							error = false;
							CODOP = st.nextToken();
							if (CODOP.matches("^[a-zA-Z].*") && CODOP.length() <= 5) {
								char[] codopar = CODOP.toCharArray();
								for (int k = 0; k < CODOP.length(); k++) {
									if (codopar[k] == '.') {
										contador_puntos++;
									}
								}
							}
							if (contador_puntos > 1) {
								System.err.println("Hay mas de un punto en el ");
								break;
							} else {
								// funcion.buscar_codop(CODOP, OPERANDO);
								// funcion.buscar_codop(CODOP);
								// modo o modos de direccionamiento, el c�digo
								// calculado, la cantidad de bytes por calcular
								// y el total de bytes que le corresponden a
								// este CODOP

							}

						} else if (linea.charAt(0) == ' ' || linea.charAt(0) == '\t') {
							if (st.countTokens() == 2) {
								contador_puntos = 0;
								error = false;
								CODOP = st.nextToken();
								if (CODOP.matches("^[a-zA-Z].*") && CODOP.length() <= 5) {
									char[] codopar = CODOP.toCharArray();

									for (int k = 0; k < CODOP.length(); k++) {
										if (codopar[k] == '.') {
											contador_puntos++;
										}
									}
								}
								if (contador_puntos > 1) {
									System.err.println("Hay mas de un punto en el codop");
									break;
								} else {

									// modo o modos de direccionamiento, el
									// c�digo calculado, la cantidad de bytes
									// por calcular y el total de bytes que le
									// corresponden a este CODOP

								}
								// FIN CODOP
								// OPERANDO

								OPERANDO = st.nextToken();
								// FIN OPERANDO
								// CODOP NADA MAS
							} else if (st.countTokens() == 1) {
								contador_puntos = 0;
								error = false;
								CODOP = st.nextToken();
								if (CODOP.matches("^[a-zA-Z].*") && CODOP.length() <= 5) {
									char[] codopar = CODOP.toCharArray();
									for (int k = 0; k < CODOP.length(); k++) {
										if (codopar[k] == '.') {
											contador_puntos++;
										}
									}
								} else {
									System.out.println("Error en la sintaxis de codop");
									break;
								}
								if (contador_puntos > 1) {
									System.err.println("Hay mas de un punto en el ");
									break;
								}
								// FIN CODOP

							}
						}

						System.out.println("\n" + "ETIQUETA: " + ETIQUETA);
						System.out.println("CODOP: " + CODOP);
						System.out.println("OPERANDO: " + OPERANDO);
						System.out.println("CONTLOC: " + CONTLOC);
						// PRIMERO VALIDAR SI ES O NO DIRECTIVA
						// AQUI DEBE DE COMPARAR LAS DIRECTIVAS Y GUARDAR EL
						// RESULTADO EN UNA VARIABLE

						String result = valdirectiva(ETIQUETA, CODOP, OPERANDO);
						File q = new File("P1tem.txt");
						BufferedWriter bwa = new BufferedWriter(new FileWriter(q, true));
						String valor = null;
						File t1 = new File("TABSIM.txt");
						BufferedWriter bwc = new BufferedWriter(new FileWriter(t1, true));
						if (comparador.equals(CODOP, "ORG")) {
							contorg++;
						} else if (result == null) {
							// SI ES NULL NO ES NINGUNA DIRECTIVA
							funcion.buscar_codop(CODOP, OPERANDO);
							int hx1 = CONTLOC;

							String str1 = Integer.toHexString(hx1);

							if (str1.length() == 4) {
								valor = str1;
							} else if (str1.length() == 3) {
								valor = "0" + str1;
							} else if (str1.length() == 2) {
								valor = "00" + str1;
							} else if (str1.length() == 1) {
								valor = "000" + str1;

							}
							bwa.write("CONTLOC" + "\t\t" + "$" + valor + "\t\t" + ETIQUETA + "\t\t" + CODOP + "\t\t"
									+ OPERANDO);
							bwa.newLine();
							bwa.close();
							if (ETIQUETA != null) {

								bwc.write(ETIQUETA + "\t\t" + "$" + valor);
								bwc.newLine();
								bwc.close();
							}
						} else if (contorg > 1) {
							// HAY MAS DE UN ORG
							System.out.println("ERROR SE DETECTARON: " + contorg
									+ " ORG SOLO DEBE EXISTIR 1 SOLO ORG EN LINEA " + (cont_lin + 1));
							break;
						} else if (result.equals("END")) {
							// SI DETECTO END DETENER LA LECTURA
							System.out.println("DETECTO END");
							System.out.println(
									++cont_lin + "_________________________________________________________________>");
							break;
						} else {
							// SI NO IMPRIMIR RESULTADO
							System.out.println(result);

							int hx1 = CONTLOC;

							String str1 = Integer.toHexString(hx1);

							if (str1.length() == 4) {
								valor = str1;
							} else if (str1.length() == 3) {
								valor = "0" + str1;
							} else if (str1.length() == 2) {
								valor = "00" + str1;
							} else if (str1.length() == 1) {
								valor = "000" + str1;

							}

							bwa.write("CONTLOC" + "\t\t" + "$" + valor + "\t\t" + ETIQUETA + "\t\t" + CODOP + "\t\t"
									+ OPERANDO);
							bwa.newLine();
							// q.deleteOnExit();
							bwa.close();

							if (ETIQUETA != null) {

								bwc.write(ETIQUETA + "\t\t" + "$" + valor);
								bwc.newLine();
								bwc.close();

								// t.deleteOnExit();
							}
						}

					}
					COMENTARIO = null;
					ETIQUETA = null;
					CODOP = null;
					OPERANDO = null;

					System.out
							.println(++cont_lin + "----------------------------------------------------------------->");
				} // while readline txt
				br.close();
				fr.close();
				funcion.codigo_maquina();
			} else {
				System.out.println("Error al abrir el archivo");
			}
		} catch (Exception e) {
		}
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		P02_STL a1 = new P02_STL();
		a1.buscar();

	}

}
