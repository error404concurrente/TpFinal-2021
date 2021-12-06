package tpMonitor;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Semaphore;

public class Cola {

	private RedDePetri rdp;
	private Queue<Hilo> espera;
	private Semaphore entrada;

	public Cola(RedDePetri rdp, Semaphore entrada) {
		this.rdp = rdp;
		espera = new ConcurrentLinkedDeque<Hilo>();
		this.entrada = entrada;
	}

	public void buscarEspera() {
		boolean encontrado = false;
		for (Hilo hilito : espera) {
			if (rdp.verificarCompatibilidad(hilito.getTarea())) {
				if (!hilito.getPolitico()) {
//					Log.spit("NO HAY POLITICA, DESPIERTO AL hilito" + hilito.getID());
					desEncolar(hilito);
					encontrado = true;
					break;
				} else {
//					Log.spit("HAY POLITICA, DECIDO POR hilito" + hilito.getID());
					if (Politicas.decidirYo(hilito)) {
//						Log.spit("LE TOCA AL hilito" + hilito.getID());
						desEncolar(hilito);
						encontrado = true;
						break;
					} else if (Politicas.estaRival(hilito, espera)) {
//						Log.spit("NO LE TOCA AL hilito" + hilito.getID());
						for (Hilo hilito2 : espera) {
							if (hilito.getIDR() == hilito2.getID()) {
								if (rdp.verificarCompatibilidad(hilito2.getTarea())) {
//									Log.spit("LE TOCA AL hilito" + hilito2.getID());
									desEncolar(hilito2);
									encontrado = true;
									break;
								} else {
//									Log.spit("LE TOCA AL hilito" + hilito.getIDR()+ " PERO NO ESTA SENSIBILIZADO");
								}
							}
						}
					} else {
//						Log.spit("LE TOCA AL OTRO HILO PERO NO ESTA");
					}
				}
			}
		}

		/*if (!encontrado) {
//		Log.spit("NO HAY HILITOS PARA DESPERTAR");
			entrada.release();
		}*/
	}

	public void encolar(Hilo hilo) throws InterruptedException {
//		Log.spit("ME VOY A MIMIR " + Thread.currentThread().getName()+"  Disparo: "+hilo.strTarea());
		espera.add(hilo);
		synchronized (hilo) {
			hilo.wait();
		}
	}

	public void desEncolar(Hilo hilito) {
//		Log.spit("HAY UN HILO PARA DESPERTAR");
		espera.remove(hilito);
		synchronized (hilito) {
			hilito.notify();
		}
	}
}
