package tpMonitor;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		Log.spit(
				"=============================== Trabajo Final de Programacion Concurrente ===============================");
		Log.spit("* Name        : tpMonitor");
		Log.spit("* Author: Vega Jimena, Chagay Vera Adriel, Klincovitzky Sebastian, Severini Alejo, Wortley Agustina");
		Log.spit("* Version     : BETA");
		Log.spit("* Description : Software de manejo de Red de Petri haciendo uso de un Monitor");
		Log.spit(
				"=========================================================================================================\n");
		
		//Matriz de incidencia
		int[][] inc = { { 0, 1, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 1, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 1, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, -1, 0, 0, 0, 7, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, -1, 0, 0, 0, 7, 0 },
				{ 0, -1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, -1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 1, 0, -1, -1, 0, 0, 0, 0, 0, 0, 1 },
				{ 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, -1, -1, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, -7, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, -7, 0 },
				{ -1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, -1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 1, -1, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0 },
				{ 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, -1 },
				{ 0, 0, 0, -1, -1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
				{ 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 1, 0, 0, 0 } };

		//Marcado de la rdp
		int[] marking = { 0, 0, 0, 8, 8, 4, 4, 0, 0, 0, 0, 1, 1, 1, 0, 0, 1, 0, 0 };
		
		//Vector de transiciones sensibilizadas
		int[] tSensibility = { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

		// Tiempo en ms
		long[] alfa = { 70, 0, 0, 0, 0, 70, 70, 70, 0, 0, 0, 0, 70, 70, 70, 70, 70 };
		long[] beta = { 0xfffffff, 0xfffffff, 0xfffffff, 0xfffffff, 0xfffffff, 0xfffffff, 0xfffffff, 0xfffffff,
				0xfffffff, 0xfffffff, 0xfffffff, 0xfffffff, 0xfffffff, 0xfffffff, 0xfffffff, 0xfffffff, 0xfffffff };

		

		// Creacion de Hilos y transiciones
		// Se entienden como tareas a las transiciones que el hilo quiere disparar
		int[][] transiciones = { { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // Arrival Rate 0 t
				{ 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // Asignar P1 1
				{ 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // Asignar P2 2
				{ 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // Empezar P1 3
				{ 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // Empezar P2 4
				{ 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // FinalizarT1P2 5 t
				{ 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // FinalizarT2P1 6 t
				{ 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // FinalizarT2P2 7 t
				{ 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0 }, // P1M1 8
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 }, // P1M2 9
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 }, // P2M1 10
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0 }, // P2M2 11
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0 }, // ProcesarT2P1 12 t
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0 }, // ProcesarT2P2 13 t
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0 }, // Solucion1 14 t
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0 }, // Solucion2 15 t
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 } };// T2 16 t

		final int N_TRANS = transiciones.length;
		
		// Creacion de Politica
		Politicas politica = new Politicas(N_TRANS, 1000);
		
		// Creacion de RpP
		RedDePetri rdp = new RedDePetri(inc, tSensibility, marking, alfa, beta);

		// Creacion de Monitor
		Monitor monitor = new Monitor(rdp, politica);
				
		ArrayList<Thread> hilos = new ArrayList<Thread>();
		
		// A cada hilo se les pasa el monitor, la tarea 1 y tarea 2 a realizar, si posee
		// politica, su ID y el ID del rival
		hilos.add(new Thread(new Hilo(monitor, transiciones[0], transiciones[0]), "Arrival Rate " + 0));
		hilos.add(new Thread(new Hilo(monitor, transiciones[1], transiciones[1]), "Asignar P1 " + 1));
		hilos.add(new Thread(new Hilo(monitor, transiciones[2], transiciones[2]), "Asignar P2 " + 2));
		hilos.add(new Thread(new Hilo(monitor, transiciones[3], transiciones[3]), "Empezar P1 " + 3));
		hilos.add(new Thread(new Hilo(monitor, transiciones[4], transiciones[4]), "Empezar P2 " + 4));
		hilos.add(new Thread(new Hilo(monitor, transiciones[5], transiciones[5]), "FinalizarT1P2 " + 5));
		hilos.add(new Thread(new Hilo(monitor, transiciones[12], transiciones[6]), "T2P1 " + 6));
		hilos.add(new Thread(new Hilo(monitor, transiciones[13], transiciones[7]), "T2P2 " + 7));
		hilos.add(new Thread(new Hilo(monitor, transiciones[8], transiciones[8]), "P1M1 " + 8));
		hilos.add(new Thread(new Hilo(monitor, transiciones[9], transiciones[9]), "P1M2 " + 9));
		hilos.add(new Thread(new Hilo(monitor, transiciones[10], transiciones[10]), "P2M1 " + 10));
		hilos.add(new Thread(new Hilo(monitor, transiciones[11], transiciones[11]), "P2M2 " + 11));
		hilos.add(new Thread(new Hilo(monitor, transiciones[14], transiciones[14]), "Solucion1 " + 14));
		hilos.add(new Thread(new Hilo(monitor, transiciones[15], transiciones[15]), "Solucion2 " + 15));
		hilos.add(new Thread(new Hilo(monitor, transiciones[16], transiciones[16]),"FinalizarT1P1 " + 16));

		// Creacion del log
		Log.spit("Red De Petri Trabajando, por favor espere...");
		Log.createLog();
		for (int i = 0; i < hilos.size(); i++) {
			hilos.get(i).start();
		}

		while (!politica.terminado()) {
			// esperando a que termine
		}

		// Ya sabemos que no hay que usarlo, pero es para que los hilos
		// no se queden durmiendo y se frene la ejecucion
		for (int i = 0; i < hilos.size(); i++) {
			hilos.get(i).stop();
		}

		Log.spit("Tareas ejecutadas en Procesador 1: " + Politicas.contador.get(3) + "\n\t- Tareas Tipo 1 ejecutadas: "
				+ Politicas.contador.get(16) + "\n\t- Tareas Tipo 2 ejecutadas: " + (Politicas.contador.get(6)));
		Log.spit("Tareas ejecutadas en Procesador 2: " + Politicas.contador.get(4) + "\n\t- Tareas Tipo 1 ejecutadas: "
				+ Politicas.contador.get(5) + "\n\t- Tareas Tipo 2 ejecutadas: " + (Politicas.contador.get(7)));
		Log.spit("Tareas guardadas en Memoria 1: " + (Politicas.contador.get(8) + Politicas.contador.get(10)));
		Log.spit("Tareas guardadas en Memoria 2: " + (Politicas.contador.get(9) + Politicas.contador.get(11)));
		Log.change2Console();
		Log.spit("Limite de " + Politicas.getLimite() + " tareas alcanzado.(Log para mas detalles)");
		Log.spit("Creando archivo de disparos efectuados...");
		Log.createLogDisparos();
		Log.change2Console();
		Log.spit("Log de Disparos creado");
		Log.spit("Fin de Programa");
	}
}
