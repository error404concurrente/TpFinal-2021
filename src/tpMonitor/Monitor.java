package tpMonitor;

import java.util.concurrent.Semaphore;

public final class Monitor {

	private RedDePetri rdp;
	private Cola colaEspera;
	private Semaphore mutex;
	private Politicas politica;

	public Monitor(RedDePetri red, Politicas politica) {
		rdp = red;
		mutex = new Semaphore(1, true);
		colaEspera = new Cola(rdp.getCantidadTransiciones());
		this.politica = politica;
	}

	/* El hilo intenta adquirir exclusion mutua de la red de petri para poder disparar la transicion
	 * 
	 * @param hilo: para acceder a la transicion que quiere disparar
	 * @throws InterruptedException: cuando no se pudo entrar al semaforo por una interrupcion
	 */
	public void enter(int[] tarea) throws InterruptedException {
		// Obtencion de los semaforos
		try {
//			Log.spit("Hilo entrante: "+Thread.currentThread().getName());
			mutex.acquire();
		} catch (InterruptedException e) {
			Log.spit("ERROR DE ENTRADA DE MONITOR AIUDA");
			e.printStackTrace();
		}
		execute(tarea);
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
	private void execute(int[] tarea) throws InterruptedException {
		if ( !Politicas.terminado() ) {
			if ( rdp.verificarSensibilidad(tarea) ) {
				if ( rdp.checkTemporal(tarea) ) { // Es compatible y esta dentro de la ventana temporal
					rdp.disparar(tarea); //Se dispara la transicion
					
					int[] espera = colaEspera.buscarEspera(); //Me devuelve las tareas que se encuentran en la cola
					espera = and(espera); //Hago una AND con el vector de transiciones sensibilizadas, quedando solo aquellas que se pueden disparar
					if( empty(espera) ) { //Si no hay nadie para despertar
						mutex.release();	//libero mutex y salgo del monitor
					}
					else {
						int despertar = politica.elegirTarea(espera);
						colaEspera.desEncolar(despertar);
					}
				}	
				else { // Es compatible pero no esta en la ventana temporal
					// Log.spit("Me voy "+Thread.currentThread().getName());
					mutex.release();
					Thread.sleep(rdp.sleepTime(tarea));
					enter(tarea);
				}
			} else { // No esta sensibilizada, se va a la cola de espera y cuando despierta entre denuevo
				// Log.spit("A la cola");
				mutex.release();
				colaEspera.encolar(tarea);
				// Log.spit("ME VOY A EJECUTAR " + Thread.currentThread().getName()+" Disparo:
				// "+hilo.strTarea());
				execute(tarea);
			}
		}
	}
	
	/* Realiza una and entre el vector pasado por argumento y el vector de transiciones sensibilizadas
	 * 
	 * @param espera: vector a realizar la and
	 * @return aux: nuevo vector con los resultados de la and
	 */
	private int[] and(int[] espera) {
		int[] sensibilizadas = rdp.getTranSensibilizadas();
		int[] aux = new int[sensibilizadas.length];
		for(int i = 0; i < sensibilizadas.length; i++) {
			aux[i] = sensibilizadas[i] & espera[i];
		}
		return aux;
	}
	
	/* Busca si el vector tiene todos sus valores en 0
	 * 
	 * @param vector: vector a ser analizado
	 * @return true: si el vector tan solo tiene valores 0
	 * @return false: si el vector tiene al menos un valor distinto de 0
	 */
	private boolean empty(int[] vector) {
		int aux = 0;
		for(int i = 0; i < vector.length; i++) {
			aux += vector[i];
		}
		if( aux == 0 ) {
			return true;
		}
		
		return false;
	}
}