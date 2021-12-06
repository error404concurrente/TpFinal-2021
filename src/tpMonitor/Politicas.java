package tpMonitor;

import java.util.Queue;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public final class Politicas {

	public static TreeMap<Integer, Integer> contador;
	private static int limite;

	public Politicas(int cantidad, int limite) {
		contador = new TreeMap<Integer, Integer>();
		setLimite(limite);
		inicializar(cantidad);
	}

	public static Boolean decidirYo(Hilo hilito) {

		if (contador.get(hilito.getID()) <= contador.get(hilito.getIDR())) {
//			Log.spit("Voy: "+contador.get(hilito.getID())+" y el otro va: "+ contador.get(hilito.getIDR()));
			return true;
		}
		return false;
	}

	public static Boolean estaRival(Hilo hilito, Queue<Hilo> espera) {

		for (Hilo hilito2 : espera) {
			if (hilito.getIDR() == hilito2.getID()) {
//				Log.spit("Voy: "+contador.get(hilito.getID())+" y el otro va: "+ contador.get(hilito.getIDR()));
				return true;
			}
		}
		return false;
	}

	public static void aumentar(Hilo hilito) {
		// Log.spit("Voy: "+contador.get(hilito.getID())+" y el otro va: "+
		// contador.get(hilito.getIDR()));
		contador.replace(hilito.getID(), contador.get(hilito.getID()), contador.get(hilito.getID()) + 1);
//		Log.spit("Ahora voy: "+contador.get(hilito.getID()));
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
