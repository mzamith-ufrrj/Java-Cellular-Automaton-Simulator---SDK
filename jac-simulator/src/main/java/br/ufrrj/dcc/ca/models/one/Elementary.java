package br.ufrrj.dcc.ca.models.one;

public class Elementary {
    private int mRule = 90;
    private int[][] mMatrix;

    private String mBoundary = "periodic";
    private int mTimestep = 100;
    private int mX_len = 100;
    private ElementaryRuleInterface mRule_func = null;
    
    public int getTimestep() {return mTimestep; }
    public int getX_Len()    {return mX_len;}

    public Elementary(int rule, int[] t0, String boundary, int timesteps){
        mRule = rule;
        mTimestep = timesteps;
        mX_len = t0.length;
        mBoundary = new String(boundary);
        mRule_func = setRule();
        mMatrix = new int[timesteps][mX_len];
        
        for (int i = 0; i < mX_len; i++){
            mMatrix[0][i] = t0[i];
        }


    }//public Elementary(int rule, int[] t0, String boundary, int timesteps){

    public int getStateCell(int x, int t){ return mMatrix[t][x]; }
    public void apply(){
        for (int t = 1; t < mTimestep; t++){
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
        }
    }//public void apply(){
    private ElementaryRuleInterface setRule(){
        ElementaryRuleInterface rule = null;
        switch (mRule) {
            case 90:
                rule = new Rule90();
                break;
        }
        return rule;
    }//private ElementaryRuleInterface setRule(){
}
