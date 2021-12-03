import java.util.ArrayList;
import java.util.Arrays;

public class PetriNet{

    private int [][] incidenceMatrix;
    private int [] sensitizedTrans;
    private int [] initMarking;
    private int [] currentMarking;

    public PetriNet(int[][] inc, int[] trans,int[] initM, int[] currentM){

        incidenceMatrix = inc;
        sensitizedTrans = trans;
        initMarking = initM;
        currentMarking = currentM;

        

        
    }
}