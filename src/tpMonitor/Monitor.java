package tpMonitor;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Semaphore;

public final class Monitor {

	private RedDePetri rdp;
	private Cola colaEspera;
	private Semaphore entrada;
	private Semaphore mutex;

	public Monitor(RedDePetri red) {
		rdp = red;
		entrada = new Semaphore(1, true);
		mutex = new Semaphore(1, true);
		colaEspera = new Cola(rdp, entrada);
	}

	/* El hilo intenta adquirir exclusion mutua de la red de petri para poder disparar la transicion
	 * 
	 * @param hilo: para acceder a la transicion que quiere disparar
	 * @throws InterruptedException: cuando no se pudo entrar al semaforo por una interrupcion
	 */
	public void enter(Hilo hilo) throws InterruptedException {
		// Obtencion de los semaforos
		try {
//			Log.spit("Hilo entrante: "+Thread.currentThread().getName());
			//entrada.acquire();
			mutex.acquire();
		} catch (InterruptedException e) {
			Log.spit("ERROR DE ENTRADA DE MONITOR AIUDA");
			e.printStackTrace();
		}
		execute(hilo);
//		Log.spit("TERMINE ME VOY " + Thread.currentThread().getName()+"  Disparo: "+hilo.strTarea());
	}

	/* Una vez conseguida la exclusion mutua, y si no se ha alcanzado el limite, verifica que la
	 * transicion este sensibilizada y dentro de la ventana de tiempo para ejecutarse.
	 * 
	 * Si se pudiera ejecutar realiza una busqueda en la cola de espera a continuacion.
	 * Si no se pudiera ejecutar por la ventana de tiempo se duerme y vuelve a intentar.
	 * Si no se pudiera ejecutar por la sensibilizacion de la transicion, se va a la cola.
	 * 
	 * @param hilo: para acceder a la transicion que quiere disparar
	 * @throws InterruptedException: cuando no se pudo entrar al semaforo por una interrupcion
	 */
	private void execute(Hilo hilo) throws InterruptedException {
		if (!Politicas.terminado()) {
			if (rdp.verificarCompatibilidad(hilo.getTarea(), hilo)) {
				if (rdp.checkTemporal(hilo)) { // Es compatible y esta dentro de la ventana temporal
					rdp.disparar(hilo);
					mutex.release();
					colaEspera.buscarEspera();
				} else { // Es compatible pero no esta en la ventana temporal
					// Log.spit("Me voy alv "+Thread.currentThread().getName());
					mutex.release();
					//entrada.release();
					Thread.sleep(rdp.mimirTime(hilo));
					enter(hilo);
				}
			} else { // No es compatible
				// Log.spit("A mimir");
				mutex.release();
				//entrada.release();
				colaEspera.encolar(hilo);
				// Log.spit("ME VOY A EJECUTAR " + Thread.currentThread().getName()+" Disparo:
				// "+hilo.strTarea());
				mutex.acquire();
				execute(hilo);
			}
		}
	}
}