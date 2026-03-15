package br.ufrrj.dcc.ca.models.one;
import java.awt.Toolkit;
import java.util.Vector;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JProgressBar;

import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;

import br.ufrrj.dcc.ca.Internal1DCA;

public class Elementary extends Thread{
    
    private final double []TABLE_K  = {3.0, 2.0, 2.0, 1.0, 2.0, 1.0, 1.0, 0.0}; 
 
    private int      mRule     = 90;
    private int[][]  mMatrix = null;
    private double[] mDensity = null;
    private double[] mEntropy = null;
    private double[] mLambda_L = null;
    private double[] mLambda_R = null;

    private String mBoundary   =  "periodic";
    private int mTimestep      = 512;
    private int mX_len         = 512;
    private boolean mIsRunning = false;
    private boolean mSetFracDim  = false;
    private TheRule mRule_func = null;
    private Internal1DCA mInternal1DCA         = null;
    

    public Elementary(Internal1DCA internal,
                      int rule, 
                      int[] t0, 
                      String boundary, 
                      int timesteps){
        mInternal1DCA = internal;
        mRule = rule;
        mTimestep = timesteps;
        mX_len = t0.length;
        mBoundary = new String(boundary);
        mRule_func = new TheRule(mRule);
        mMatrix = new int[timesteps][mX_len];
        
        for (int i = 0; i < mX_len; i++){
            mMatrix[0][i] = t0[i];
        }

        mDensity = new double[timesteps];
        mEntropy = new double[timesteps];
        mLambda_L = new double[timesteps];
        mLambda_R = new double[timesteps];


    }//public Elementary(int rule, int[] t0, String boundary, int timesteps){

    public void setWhiteNoise(double p, boolean rand){
        
        if (!rand){
            mDensity[0] = 0.0f; 
            return;
        }
        mDensity[0] = 0.0;
        String binaryword = mRule_func.getBinary();
        for (int i = 0; i < binaryword.length(); i++){
            if (binaryword.charAt(i) == '1'){
                double k = TABLE_K[i];
                mDensity[0] += (Math.pow(p, k) * Math.pow(1.0-p, 3.0-k)); 

            }
        }//for (int i = 0; i < binaryword.length(); i++){
        
    }//public void setWhiteNoise(double p, boolean rand){

    public synchronized void setIsRunningTrue() {mIsRunning = true;}
    public synchronized void setIsRunningFalse() {mIsRunning = false;}
    public synchronized boolean getIsRunning() {return mIsRunning;}

    public int getTimestep() {return mTimestep; }
    public int getX_Len()    {return mX_len;}
    public String getRuleName() {
        if (mRule_func == null) return "NO RULE";
        return mRule_func.getRuleName();
    }

    @Override
    public void run() {
        this.setIsRunningTrue();
        JFrame frame = new JFrame("Simulation progress");
        JProgressBar progressbar = new JProgressBar(0, this.mTimestep);
        progressbar.setStringPainted(true);
        frame.add(progressbar);
            
        frame.setSize(400, 60);
        frame.setVisible(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        int w = (int)(screenSize.getWidth() / 2) - 200;
        int h = (int)(screenSize.getHeight() / 2) - 30;
        frame.setResizable(false);
        frame.setLocation(w, h);
        progressbar.setValue(0);
        int ts = 1;
        while(this.getIsRunning() && ts < this.mTimestep) {

            update(ts);
            setStatistic(ts);
            progressbar.setValue(ts);
            ts++;
            
        }
        if (mSetFracDim)
            fractalDimension();

        this.setIsRunningFalse();
        progressbar.setValue(ts);

        try { Thread.sleep(100); } 
        catch (InterruptedException e) {e.printStackTrace();}
        
        frame.setVisible(false);
        frame = null;
        
        mInternal1DCA.finish();
    }

    private void update(int t){

        for (int x = 0; x < mX_len; x++){
            int x_l = -1,
                x_r = -1,
                x_c = mMatrix[t-1][x];

            if (x > 0){
                x_l = mMatrix[t-1][x - 1];
            }else if (mBoundary.compareTo("reflexive") == 0){
                x_l = mMatrix[t-1][x + 1];
            }else if (mBoundary.compareTo("constant [0]") == 0){
                x_l = 0;
            }else if (mBoundary.compareTo("constant [1]") == 0){
                x_l = 1;
            }else if (mBoundary.compareTo("periodic") == 0){
                x_l =  mMatrix[t-1][mX_len - 1];
            }

            if (x < mX_len - 1){
                x_r = mMatrix[t-1][x + 1];
            }else if (mBoundary.compareTo("reflexive") == 0){
                x_r = mMatrix[t-1][x - 1];
            }else if (mBoundary.compareTo("constant [0]") == 0){
                x_r = 0;
            }else if (mBoundary.compareTo("constant [1]") == 0){
                x_r = 1;
            }else if (mBoundary.compareTo("periodic") == 0){
                x_r =  mMatrix[t-1][0];
            }

            assert x_l != -1 || x_c != -1 || x_r != -1: "Error: Values are not defined";

            mMatrix[t][x] = mRule_func.apply(x_l, x_c, x_r);
            
            
        }   
       
    }//public void apply(){
    public void setFractalDimension(){ this.mSetFracDim = true; };

    private double box_count(int s_boxe){
        int r_boxes = mTimestep / s_boxe;
        int c_boxes =    mX_len / s_boxe;
        int count = 0;
        for (int r = 0; r < r_boxes; r++) {
            for (int c = 0; c < c_boxes; c++) {
                boolean occupied = false;
                for (int i = 0; i < s_boxe; i++) {
                    for (int j = 0; j < s_boxe; j++) {
                        int my_time = (r * s_boxe + i);
                        int my_x = (c * s_boxe + j);
                        //assert my_time <= (2*8192)-1 : "Error tipo 1";
                        //assert my_time <= 8192-1 : "Error tipo 1";
                        if (mMatrix[my_time][my_x] == 1) {
                            occupied = true;
                            break;
                        }
                    }
                    if (occupied) break;
                }
                if (occupied) count++;
            }// for (int c = 0; c < c_boxes; c++) {
        }//for (int r = 0; r < r_boxes; r++) {
        return (double)count;
    }

    private void fractalDimension(){
        double pow2 = Math.log(2.0);
        int n_scales = (int) (Math.log((double)mTimestep) / pow2);
        WeightedObservedPoints obs = new WeightedObservedPoints();
        
        for (int scale = 0; scale < n_scales; scale++){
            double r = (double) (1 << scale);
            double c = box_count((int)r);
            System.out.print("(");
            System.out.print(Math.log(1.0/r));
            System.out.print(",\t");
            System.out.print(Math.log(c));
            System.out.println(")");
            obs.add(Math.log(1.0/r), Math.log(c));
        }//for (int scale = 0; scale < n_scales; scale++){
        PolynomialCurveFitter fitter = PolynomialCurveFitter.create(1);
        double[] coeff = fitter.fit(obs.toList());
        for (int i = 0; i < coeff.length; i++){
            System.out.println(coeff[i]);
        }
        mSetFracDim = false;
    }
    
    private void setStatistic(int t){
        double prob1 = 0.0, prob0 = 0.0, n = (double) mX_len;
        for (int i = 0; i < mX_len; i++){
            if (mMatrix[t][i] == 1) prob1++;
            if (mMatrix[t][i] == 0) prob0++;
        }
        double a = prob1 / n;
        double b = prob0 / n;
        double c =  Math.log(2.0);
        mEntropy[t]  = ((Math.log(a) / c) * a);
        mEntropy[t] += ((Math.log(b) / c) * b);
        mEntropy[t] *= -1.0;



        if (t == 1){
            mDensity[t] = mRule_func.getB1s() / 8.0;
            return;
        }
        
        mDensity[t] = prob1 / n;

    }
    public String getInfo(){
        if (mRule_func == null) return "NO RULE";
        String info = new String("# " + mRule_func.getRuleName() + "\n");
        info += "#          Mesh length: " + Integer.toString(mX_len) + "\n";
        info += "#            Timesteps: " + Integer.toString(mTimestep) + "\n";
        info += "# Boundaries condition:"  + mBoundary + "\n";
        info += "#-------------------------------------------------------------------------------\n";
        info += "timestep;density;entropy;lambdaL;lambdaR\n";
        for (int i = 0; i < mTimestep; i++){
            info += String.format("%d;%.10f;%.10f;%.10f;%.10f\n", 
            i,
            mDensity[i], 
            mEntropy[i], 
            mLambda_L[i], 
            mLambda_R[i]);
        }
        return info;
    }

    public int getStateCell(int x, int t){ return mMatrix[t][x]; }
  
}
