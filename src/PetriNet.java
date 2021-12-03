import java.util.ArrayList;
import java.util.Arrays;

import Jama.Matrix;

public class PetriNet{

    private Matrix incidenceMatrix;
    private Matrix sensitizedTrans;
    private Matrix initMarking;
    private Matrix currentMarking;
 

    public PetriNet(int[][] inc, int[] trans,int[] initM, int[] currentM){

        incidenceMatrix = new Matrix(inc);
        sensitizedTrans = new Matrix(1, trans.lenght, trans);
        initMarking     = new Matrix(1, initM.length, initM);
        currentMarking  = new Matrix(1, currentM.length, currentM);
    
    }
    /**
     * Actualiza transiciones sensibilizadas
     */
    public updateSensitizedTrans(){

        for(int j=0; j<incidenceMatrix.getColumnDimension(); j++){

            for(int i=0; i<incidenceMatrix.getRowDimension(); i++){
               
                if((incidenceMatrix.get(i,j) >= currentMarking.get(i,j)) && (incidenceMatrix.get(i,j) < 0)){
                    sensitizedTrans.set(0, j, 1);
                }
                else{
                    sensitizedTrans.set(0, j, 0);
                }
            }
        }
    }
    /**
     * Actualiza marcado de la rdp tras un disparo exitoso.
     * m = m0 + I ·σ.
     * @param sigma : vector de disparo
     */
    public updateCurrentMarking(Matrix sigma){

        
    }

 


  
}