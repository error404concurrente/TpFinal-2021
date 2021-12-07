package tpMonitor;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Cola {

	private int numeroTransiciones;
	private ArrayList<Semaphore> cola;

	public Cola(int numeroTransiciones) {
		this.numeroTransiciones = numeroTransiciones;
		cola = new ArrayList<Semaphore>();
		inicializarCola();
	}

	private void inicializarCola(){
		for(int i = 0; i < numeroTransiciones; i++) {
			cola.add(new Semaphore(0));
		}
	}
	
	/* Realiza una busqueda en la cola de espera para ver quienes se encuentran, y devuelve un array de
	 * enteros del mismo largo que el de vector de transiciones. Dado que cada semaforo de la cola
	 * fue inicializado en 0 y que hay tan solo un hilo por transicion, si hay alguno en la cola
	 * sera aquel que este esperando en la cola del semaforo, por lo tanto utilizando getQueueLenght
	 * dara 0 cuando no haya ningun hilo esperando en la posicion que representaria a su transicion
	 * y dara 1 cuando si lo este
	 * 
	 * @return vector de enteros que indica los hilos de que transicion se encuentran esperando
	 */
	public int[] buscarEspera() {
		int[] encolados = new int[numeroTransiciones];
   	 	for(int i = 0; i < numeroTransiciones; i++) {
            encolados[i] = cola.get(i).getQueueLength();
   	 	}
		
		return encolados;
	}

	/* En base a la transicion que representa la tarea, intentara adquirir el semaforo correspondiente
	 * y quedara en espera dado que el semaforo fue inicializado con 0
	 * 
	 * @param tarea: vector que representa la transicion que dispara el hilo
	 */
	public void encolar(int[] tarea) throws InterruptedException {
//		Log.spit("ME VOY A LA COLA " + Thread.currentThread().getName()+"  Disparo: "+hilo.strTarea());
		int indice = 0;
		for(int i = 0; i < tarea.length; i++) {
			if( tarea[i] == 1 ) {
				indice = i;
				break;
			}
		}
		
		cola.get(indice).acquire();
	}

	/* En base a un indice representativo realiza un release de un semaforo, permitiendo que el hilo
	 * que anteriormente habia quedado dormido al hacer el acquire pueda seguir con su ejecucion
	 * 
	 * @param indice: el indice representativo del hilo a despertar
	 */
	public void desEncolar(int indice) {
//		Log.spit("HAY UN HILO PARA DESPERTAR");
		cola.get(indice).release();
	}
}
