package tpMonitor;

public class Hilo implements Runnable {

	private Monitor monitor;
	private int[] secundario;
	private int[] tarea;

	public Hilo(Monitor monitor, int[] tarea, int[] secundario) {
		this.monitor = monitor;
		this.tarea = tarea;
		this.secundario = secundario;
	}

	/* Al ser un hilo, este metodo sera llamado automaticamente cuando el hilo reciba start
	 * 
	 * Mientras no se haya alcanzado el limite de ejecuciones, el hilo intentara entrar al monitor
	 * para ejecutar su tarea.
	 * Una vez salga del monitor, realizara un cambio entre la tarea principal y la secundaria.
	 */
	public void run() {
		while (!Politicas.terminado()) {
			try {
				monitor.enter(tarea);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			int[] aux = tarea;
			tarea = secundario;
			secundario = aux;
		}
	}

	public String strTarea() {
		String v = "";
		for (int i = 0; i < tarea.length; i++) {
			v = v + tarea[i] + ", ";
		}
		return v;
	}

	public int[] getTarea() {
		return tarea;
	}
}