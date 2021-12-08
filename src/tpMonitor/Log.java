package tpMonitor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Log {
	private static final SimpleDateFormat sdfTitulo = new SimpleDateFormat("[dd_MM_yyyy]-[HH_mm_ss]");
	public static PrintStream archivo;
	public static ArrayList<Integer> disparos = new ArrayList<Integer>();
	public static PrintStream consola;

	/* Instancia un archivo con la fecha y hora y guarda la consola que este en uso.
	 * Redirige el output de la consola al archivo.
	 * 
	 * @throws FileNotFoundException
	 */
	public static void createLog() throws FileNotFoundException {
		consola = System.out;
		Date resultdate = new Date(System.currentTimeMillis());
		archivo = new PrintStream(new File("log" + sdfTitulo.format(resultdate) + ".txt"));
		System.setOut(archivo);
	}

	/* Funcion global para imprimir timestamps en consola, y por lo tanto en el log
	 * 
	 * @return String: devuelve un string con la hora formateada
	 */
	public static String ts() {
		SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm:ss:SSS]: ");
		Date resultdate = new Date(System.currentTimeMillis());
		return sdf.format(resultdate);
	}

	/* Esta funcion es llamada para imprimir en consola y en el log.
	 * 
	 * @param str: El string que sera impreso
	 */
	public static void spit(String str) {
		System.out.println(ts() + str);
	}

	/* Guarda en el Arraylist disparos las transiciones ejecutadas para su postproceso con regex
	 * 
	 * @param disparo: vector de enteros que representa la transicion disparada
	 */
	public static void addDisparo(int[] disparo) {
		for (int i = 0; i < disparo.length; i++) {
			if (disparo[i] == 1) {
				disparos.add(i);
			}
		}
	}

	/* Crea el archivo que guardara los disparos efectuados durante la ejecucion del programa.
	 * 
	 * @throws FileNotFoundException
	 */
	public static void createLogDisparos() throws FileNotFoundException {
		archivo = new PrintStream(new File("disparos.txt"));
		System.setOut(archivo);
		for (int i = 0; i < disparos.size(); i++) {
			System.out.print(disparos.get(i) + " ");
		}
	}

	/* Para poder cambiar el output del log a la consola cuando fuera necesasrio.
	 * 
	 */
	public static void change2Console() {
		System.setOut(consola);
	}

}
