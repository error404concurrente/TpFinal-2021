import java.util.ArrayList;
import java.util.Arrays;

import Jama.Matrix;

public class PetriNet{

    private Matrix incidenceMatrix;
    private Matrix sensitizedTrans;
    private Matrix initMarking;
    private Matrix currentMarking;
 

    public PetriNet(int[][] inc, int[] trans,int[] marking){

        incidenceMatrix = new Matrix(inc);
        //new Matrix(1, trans.lenght, trans);
        initMarking     = new Matrix(1, marking.length, marking);
        currentMarking  = new Matrix(1, marking.length, marking);
        updateSensitizedTrans();
    
    }
    /**
     * Actualiza transiciones sensibilizadas
     */
    public void updateSensitizedTrans(){

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

        for(int i=0; i<sensitizedTrans.getColumnDimension(); i++){
            System.out.println(sensitizedTrans.get(i,j));
        }

    }
    /**
     * Actualiza marcado de la rdp tras un disparo exitoso.
     * m = m0 + I ·σ.
     * @param firing : vector de disparo
     */
    public void updateCurrentMarking(Matrix firing){

        
    }

 


  
}