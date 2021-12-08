package tpMonitor;


public class RedDePetri {

	private int[][] matrizIncidencia;
	private int[] tranSensibilizadas;
	private long[] transTimestamp;
	private int[] marcaInicial;
	private int[] marcaActual;
	private long[] alfa;
	private long[] beta;
	private int[][] pinvariant = {  { 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
									{ 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
									{ 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
									{ 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
									{ 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0 },
									{ 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1 },
									{ 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
									{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1 } };

	public RedDePetri(int[][] inc, int[] trans, int[] act, long[] alfa, long[] beta) {
		matrizIncidencia = inc;
		tranSensibilizadas = trans;
		marcaInicial = act;
		marcaActual = act;
		this.alfa = alfa;
		this.beta = beta;
		transTimestamp = new long[tranSensibilizadas.length];

		for (int i = 0; i < transTimestamp.length; i++) {
			transTimestamp[i] = System.currentTimeMillis();
		}
	}
	
	/* Un hilo que haya llegado aqui consiguio la exclusion mutua y disparara su transicion asociada.
	 * Se actualizara el vector de marca actual y el de vectores sensibilizados.
	 * Adicionalmente llamara a funciones de contador para la ejecucion del programa e impresion 
	 * del log
	 * 
	 * @param tarea: La transicion a ser disparada
	 * @throws InterruptedException
	 */
	public void disparar(int[] tarea) throws InterruptedException {
		calcularMarcaActual(tarea);
		calcularVectorSensible();
		Log.spit("[Disparo Efectuado]: " + Thread.currentThread().getName() + "  [Estado Actual]: " + strMarcaActual()
				+ " [T.Sensibilizadas]: " + strTranSensible());
		Politicas.aumentar(tarea);
		Log.addDisparo(tarea);
		showPinvariants(pinvariant, marcaActual);
	}

	/* Tras haber adquirido mutex, verifica si esta sensibilizada la transicion que quiere disparar
	 * 
	 * @param tarea: vector de enteros que indica la transicion que quiere disparar
	 * @return true: si la transicion se puede ejecutar sin problema
	 * @return false: si la transicion no puede ser ejecutada en este momento
	 */
	public boolean verificarSensibilidad(int[] tarea) {
		boolean compatible = false;
		for (int i = 0; i < tranSensibilizadas.length; i++) {
			if (tranSensibilizadas[i] == 1 && tarea[i] == 1) {
				compatible = true;
			}
		}
		return compatible;
	}

	/* Realiza calculo para nueva marca habiendo disparado una transicion
	 * 
	 * Para realizar el calculo, utiliza la matriz de incidencia, donde realiza
	 * una multiplicacion de cada una de sus columnas con el vector de la transicion
	 * acumulando el resultado en una variable temporal.
	 * El resultado de hacer esto en cada columna, sera el nuevo valor que habra en
	 * la nueva marca de la red.
	 * 
	 * @param disparo: vector de enteros con la transicion que se va a disparar
	 */
	private void calcularMarcaActual(int[] disparo) {
		int aux;
		for (int i = 0; i < matrizIncidencia.length; i++) {
			aux = 0;
			for (int j = 0; j < matrizIncidencia[0].length; j++) {
				aux += matrizIncidencia[i][j] * disparo[j]; // Ecuacion de Estado
			}
			marcaActual[i] += aux;
		}
	}

	/* Calcula el nuevo vector de transiciones sensibilizadas
	 * 
	 * Tras haberse disparado la transicion, se calcula el nuevo vector de transiciones sensibilizadas
	 * y ademas actualiza el vector con los tiempos actuales para las transiciones temporales
	 * que se acaban de sensibilizar.
	 * 
	 */
	private void calcularVectorSensible() {
		int[] s = new int[marcaActual.length]; // vector auxiliar S
		int[] aux = new int[tranSensibilizadas.length];
		for (int i = 0; i < aux.length; i++) {
			aux[i] = tranSensibilizadas[i];
		}

		for (int i = 0; i < matrizIncidencia[0].length; i++) { // Recorrer columnas
			for (int j = 0; j < matrizIncidencia.length; j++) { // Recorrer filas
				s[j] = marcaActual[j] + matrizIncidencia[j][i]; // Calculo de S
			}

			// Crear nuevo vector de transiciones sensibilizadas En la posicion i va a ser 1
			// si no hay valores negativos en S, de otra forma ser 0
			tranSensibilizadas[i] = 1;
			for (int x = 0; x < s.length; x++) {
				if (s[x] < 0) {
					tranSensibilizadas[i] = 0;
					break;
				}
			}
		}
		//Actualizo timestamp de vectores sensibilizados y desenzibilizados
		for (int i = 0; i < tranSensibilizadas.length; i++) {
			if (aux[i] == 0 && tranSensibilizadas[i] == 1) {
				transTimestamp[i] = System.currentTimeMillis();
			} else if (aux[i] == 1 && tranSensibilizadas[i] == 0) {
				transTimestamp[i] = 0;
			}
		}
	}

	/* Chequea que la transicion que quiere disparar se encuentre dentro de la ventana temporal
	 * 
	 * @param hilo: para acceder a la transicion que quiere disparar
	 * @return true: si el hilo se encuentra dentro de la ventana temporal y puede dispararse
	 * @return false: si el hilo no se encuentra dentro de la ventana y no puede dispararse
	 */
	public boolean checkTemporal(int[] tarea) throws InterruptedException {
		for (int i = 0; i < tarea.length; i++) {
			if (tarea[i] == 1) {
				long tSensible = System.currentTimeMillis() - transTimestamp[i];
				long t = alfa[i] - tSensible;
				if (t > 0) {
					return false;
				} else if (t <= 0 && beta[i] >= tSensible) {
					return true;
				} else if (t <= 0 && beta[i] < tSensible) {
					Log.spit(beta[i] + " ");
					return false;
				}
			}
		}
		return true;
	}

	/* Calcula tiempo para dormir del hilo para que se encuentre dentro de la ventana temporal
	 * 
	 * @param tarea: para acceder a la transicion que quiere disparar
	 * @return long: cantidad de tiempo que dormira el hilo en base a su ventana temporal
	 * @return 1000: Si por algun motivo se hubiera llamado a esta funcion con una transicion
	 * 				 que no estuviera sensibilizada, este sera el valor por defecto devuelto
	 * 				 por caso de que hubiera algun error
	 */
	public long sleepTime(int[] tarea) {
		for (int i = 0; i < tarea.length; i++) {
			if (tarea[i] == 1) {
				long tSensible = System.currentTimeMillis() - transTimestamp[i];
				long t = alfa[i] - tSensible;
				return t;
			}
		}
		return 1000;
	}

	/* Luego de cada disparo, muestra que se cumplan los p invariantes
	 * y por lo tanto que no hay problemas en la red
	 */
	public void showPinvariants(int[][] pinvariant, int[] m) {
		String s = "";
		boolean first = false;
		int k = 0;
		for (int i = 0; i < pinvariant.length; i++) {
			first = true;
			for (int j = 0; j < pinvariant[0].length; j++) {

				if (pinvariant[i][j] != 0 && first) {
					s += " P" + j + "(" + m[j] + ") ";
					k += m[j];
					first = false;
				} else if (pinvariant[i][j] != 0) {
					k += m[j];
					s += " +  P" + j + "(" + m[j] + ")";
				}
			}
			s += " = " + k;
			Log.spit(s);
			k = 0;
			s = "";
		}
	}

	// Getters
	public int[] getMarcaActual() {
		return marcaActual;
	}

	public int[][] getMatrizIncidencia() {
		return matrizIncidencia;
	}

	public int[] getMarcaInicial() {
		return marcaInicial;
	}

	public String strTranSensible() {
		String v = "";
		for (int i = 0; i < tranSensibilizadas.length; i++) {
			v = v + tranSensibilizadas[i] + ", ";
		}
		return v;
	}

	public String strMarcaActual() {
		String v = "";
		for (int i = 0; i < marcaActual.length; i++) {
			v += marcaActual[i] + ", ";
		}
		return v;
	}

	public int[] getTranSensibilizadas() {
		return tranSensibilizadas;
	}
	
	public int getCantidadTransiciones() {
		return tranSensibilizadas.length;
	}
}
