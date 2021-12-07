package tpMonitor;

import java.util.TreeMap;
import java.util.Random;

public final class Politicas {

	public static TreeMap<Integer, Integer> contador;
	private static int limite;

	public Politicas(int cantidad, int limite) {
		contador = new TreeMap<Integer, Integer>();
		setLimite(limite);
		inicializar(cantidad);
	}
	
	/* En base a un vector con las tareas que se encuentran en la cola de espera y que estan sensibilizados
	 * se elige de manera aleatoria una tarea para ser despertada
	 * 
	 * @param espera: vector con tareas a elegir
	 * @return indice de la transicion elegida
	 */
	public int elegirTarea(int[] espera) {
		int aux = 0;
		for(int i = 0; i < espera.length; i++) {
			aux += espera[i];
		}
		
		int indice;
		Random random = new Random();
		aux = random.nextInt(aux) + 1;
		for(indice = 0; indice < espera.length; indice++) {
			if( espera[indice] == 1 ) {
				aux--;
				if( aux == 0 ) {
					return indice;
				}
			}
		}
		
		return indice;
	}

	/* Aumenta contador de veces que se ejecuto la tarea
	 * 
	 * @param tarea: tarea que se ejecuto
	 */
	public static void aumentar(int[] tarea) {
		for (int i = 0; i < tarea.length; i++) {
			if (tarea[i] == 1) {
				//Log.spit("Tarea: "+ Thread.currentThread().getName() + "iba ejecutandose: " + contador.get(i));
				contador.replace(i, contador.get(i), contador.get(i) + 1);
				//Log.spit("Ahora va" + contador.get(i));
				break;
			}
		}
	}

	private void inicializar(int cantidad) {
		for (int i = 0; i < cantidad; i++) {
			contador.put(i, 0);
		}
	}

	public static boolean terminado() {
		if (getMil() < getLimite()) {
			return false;
		} else {
			return true;
		}
	}

	public static int getMil() {
		int a = contador.get(5);
		int b = contador.get(6) / 2;
		int c = contador.get(7) / 2;
		int d = contador.get(16);
		return a + b + c + d;
	}

	public static int getLimite() {
		return limite;
	}

	private static void setLimite(int limite) {
		Politicas.limite = limite;
	}
}
