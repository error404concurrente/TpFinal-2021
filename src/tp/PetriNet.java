package tp;
import java.util.ArrayList;
import java.util.Arrays;
import Jama.Matrix;

public class PetriNet{

    private Matrix incidenceMatrix;
    private Matrix sensitizedTrans;
    private Matrix initMarking;
    private Matrix currentMarking;
 

    public PetriNet(double[][] inc, int[] trans,double[] marking){

        incidenceMatrix = new Matrix(inc);

        initMarking     = new Matrix(marking, 1);
        currentMarking  = new Matrix(marking, 1);
        sensitizedTrans = new Matrix(1, incidenceMatrix.getColumnDimension());
        updateSensitizedTrans();
    
    }
    /**
     * Actualiza transiciones sensibilizadas
     */
    public void updateSensitizedTrans(){
        boolean enableSensitizedTrans;

        //long currentTime = System.currentTimeMillis(); //Establezco el tiempo una sola vez para denotar que todas las transiciones se sensibilizaron "al mismo tiempo".

        for(int j = 0; j < incidenceMatrix.getColumnDimension(); j++) {
            enableSensitizedTrans = true;
            
            for(int i = 0; i < incidenceMatrix.getRowDimension(); i++)
                if((incidenceMatrix.get(i, j)*(-1)) > currentMarking.get(0, i)) {
                    enableSensitizedTrans = false;
                    break;
                }
            
            if(enableSensitizedTrans) {
                sensitizedTrans.set(0, j, 1);
                //setEnabledAtTime(j, currentTime);
            }
            else {
                sensitizedTrans.set(0, j, 0);
            }

            
        }
        sensitizedTrans.print(sensitizedTrans.getRowDimension(),0);
    }
    /**
     * Actualiza marcado de la rdp tras un disparo exitoso.
     * m = m0 + I ·σ.
     * @param firing : vector de disparo
     */
    public void updateCurrentMarking(Matrix firing){

        
    }

 


  
}