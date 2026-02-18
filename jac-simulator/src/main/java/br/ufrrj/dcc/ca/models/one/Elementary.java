package br.ufrrj.dcc.ca.models.one;
import java.awt.Toolkit;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JProgressBar;

import br.ufrrj.dcc.ca.Internal1DCA;

public class Elementary extends Thread{
    private int mRule       = 90;
    private int[][] mMatrix = null;

    private String mBoundary   =  "periodic";
    private int mTimestep      = 100;
    private int mX_len         = 100;
    private boolean mIsRunning = false;
    private ElementaryRuleInterface mRule_func = null;
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
        mRule_func = setRule();
        mMatrix = new int[timesteps][mX_len];
        
        for (int i = 0; i < mX_len; i++){
            mMatrix[0][i] = t0[i];
        }


    }//public Elementary(int rule, int[] t0, String boundary, int timesteps){


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
            progressbar.setValue(ts);
            ts++;
        }
        this.setIsRunningFalse();
        progressbar.setValue(ts);

        try { Thread.sleep(100); } 
        catch (InterruptedException e) {e.printStackTrace();}
        
        frame.setVisible(false);
        frame = null;
        
        mInternal1DCA.finish();
    }

    public void update(int t){
    
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

    public String getInfo(){
        if (mRule_func == null) return "NO RULE";
        String info = new String(mRule_func.getRuleName() + "\n");
        info += "          Mesh length: " + Integer.toString(mX_len) + "\n";
        info += "            Timesteps: " + Integer.toString(mTimestep) + "\n";
        info += " Boundaries condition:"  + mBoundary + "\n";

        return info;
    }

    public int getStateCell(int x, int t){ return mMatrix[t][x]; }
    
    private ElementaryRuleInterface setRule(){
        ElementaryRuleInterface rule = null;
        switch (mRule) {
              case 0: rule = new Rule_000();break;
              case 1: rule = new Rule_001();break;
              case 2: rule = new Rule_002();break;
              case 3: rule = new Rule_003();break;
              case 4: rule = new Rule_004();break;
              case 5: rule = new Rule_005();break;
              case 6: rule = new Rule_006();break;
              case 7: rule = new Rule_007();break;
              case 8: rule = new Rule_008();break;
              case 9: rule = new Rule_009();break;
              case 10: rule = new Rule_010();break;
              case 11: rule = new Rule_011();break;
              case 12: rule = new Rule_012();break;
              case 13: rule = new Rule_013();break;
              case 14: rule = new Rule_014();break;
              case 15: rule = new Rule_015();break;
              case 16: rule = new Rule_016();break;
              case 17: rule = new Rule_017();break;
              case 18: rule = new Rule_018();break;
              case 19: rule = new Rule_019();break;
              case 20: rule = new Rule_020();break;
              case 21: rule = new Rule_021();break;
              case 22: rule = new Rule_022();break;
              case 23: rule = new Rule_023();break;
              case 24: rule = new Rule_024();break;
              case 25: rule = new Rule_025();break;
              case 26: rule = new Rule_026();break;
              case 27: rule = new Rule_027();break;
              case 28: rule = new Rule_028();break;
              case 29: rule = new Rule_029();break;
              case 30: rule = new Rule_030();break;
              case 31: rule = new Rule_031();break;
              case 32: rule = new Rule_032();break;
              case 33: rule = new Rule_033();break;
              case 34: rule = new Rule_034();break;
              case 35: rule = new Rule_035();break;
              case 36: rule = new Rule_036();break;
              case 37: rule = new Rule_037();break;
              case 38: rule = new Rule_038();break;
              case 39: rule = new Rule_039();break;
              case 40: rule = new Rule_040();break;
              case 41: rule = new Rule_041();break;
              case 42: rule = new Rule_042();break;
              case 43: rule = new Rule_043();break;
              case 44: rule = new Rule_044();break;
              case 45: rule = new Rule_045();break;
              case 46: rule = new Rule_046();break;
              case 47: rule = new Rule_047();break;
              case 48: rule = new Rule_048();break;
              case 49: rule = new Rule_049();break;
              case 50: rule = new Rule_050();break;
              case 51: rule = new Rule_051();break;
              case 52: rule = new Rule_052();break;
              case 53: rule = new Rule_053();break;
              case 54: rule = new Rule_054();break;
              case 55: rule = new Rule_055();break;
              case 56: rule = new Rule_056();break;
              case 57: rule = new Rule_057();break;
              case 58: rule = new Rule_058();break;
              case 59: rule = new Rule_059();break;
              case 60: rule = new Rule_060();break;
              case 61: rule = new Rule_061();break;
              case 62: rule = new Rule_062();break;
              case 63: rule = new Rule_063();break;
              case 64: rule = new Rule_064();break;
              case 65: rule = new Rule_065();break;
              case 66: rule = new Rule_066();break;
              case 67: rule = new Rule_067();break;
              case 68: rule = new Rule_068();break;
              case 69: rule = new Rule_069();break;
              case 70: rule = new Rule_070();break;
              case 71: rule = new Rule_071();break;
              case 72: rule = new Rule_072();break;
              case 73: rule = new Rule_073();break;
              case 74: rule = new Rule_074();break;
              case 75: rule = new Rule_075();break;
              case 76: rule = new Rule_076();break;
              case 77: rule = new Rule_077();break;
              case 78: rule = new Rule_078();break;
              case 79: rule = new Rule_079();break;
              case 80: rule = new Rule_080();break;
              case 81: rule = new Rule_081();break;
              case 82: rule = new Rule_082();break;
              case 83: rule = new Rule_083();break;
              case 84: rule = new Rule_084();break;
              case 85: rule = new Rule_085();break;
              case 86: rule = new Rule_086();break;
              case 87: rule = new Rule_087();break;
              case 88: rule = new Rule_088();break;
              case 89: rule = new Rule_089();break;
              case 90: rule = new Rule_090();break;
              case 91: rule = new Rule_091();break;
              case 92: rule = new Rule_092();break;
              case 93: rule = new Rule_093();break;
              case 94: rule = new Rule_094();break;
              case 95: rule = new Rule_095();break;
              case 96: rule = new Rule_096();break;
              case 97: rule = new Rule_097();break;
              case 98: rule = new Rule_098();break;
              case 99: rule = new Rule_099();break;
              case 100: rule = new Rule_100();break;
              case 101: rule = new Rule_101();break;
              case 102: rule = new Rule_102();break;
              case 103: rule = new Rule_103();break;
              case 104: rule = new Rule_104();break;
              case 105: rule = new Rule_105();break;
              case 106: rule = new Rule_106();break;
              case 107: rule = new Rule_107();break;
              case 108: rule = new Rule_108();break;
              case 109: rule = new Rule_109();break;
              case 110: rule = new Rule_110();break;
              case 111: rule = new Rule_111();break;
              case 112: rule = new Rule_112();break;
              case 113: rule = new Rule_113();break;
              case 114: rule = new Rule_114();break;
              case 115: rule = new Rule_115();break;
              case 116: rule = new Rule_116();break;
              case 117: rule = new Rule_117();break;
              case 118: rule = new Rule_118();break;
              case 119: rule = new Rule_119();break;
              case 120: rule = new Rule_120();break;
              case 121: rule = new Rule_121();break;
              case 122: rule = new Rule_122();break;
              case 123: rule = new Rule_123();break;
              case 124: rule = new Rule_124();break;
              case 125: rule = new Rule_125();break;
              case 126: rule = new Rule_126();break;
              case 127: rule = new Rule_127();break;
              case 128: rule = new Rule_128();break;
              case 129: rule = new Rule_129();break;
              case 130: rule = new Rule_130();break;
              case 131: rule = new Rule_131();break;
              case 132: rule = new Rule_132();break;
              case 133: rule = new Rule_133();break;
              case 134: rule = new Rule_134();break;
              case 135: rule = new Rule_135();break;
              case 136: rule = new Rule_136();break;
              case 137: rule = new Rule_137();break;
              case 138: rule = new Rule_138();break;
              case 139: rule = new Rule_139();break;
              case 140: rule = new Rule_140();break;
              case 141: rule = new Rule_141();break;
              case 142: rule = new Rule_142();break;
              case 143: rule = new Rule_143();break;
              case 144: rule = new Rule_144();break;
              case 145: rule = new Rule_145();break;
              case 146: rule = new Rule_146();break;
              case 147: rule = new Rule_147();break;
              case 148: rule = new Rule_148();break;
              case 149: rule = new Rule_149();break;
              case 150: rule = new Rule_150();break;
              case 151: rule = new Rule_151();break;
              case 152: rule = new Rule_152();break;
              case 153: rule = new Rule_153();break;
              case 154: rule = new Rule_154();break;
              case 155: rule = new Rule_155();break;
              case 156: rule = new Rule_156();break;
              case 157: rule = new Rule_157();break;
              case 158: rule = new Rule_158();break;
              case 159: rule = new Rule_159();break;
              case 160: rule = new Rule_160();break;
              case 161: rule = new Rule_161();break;
              case 162: rule = new Rule_162();break;
              case 163: rule = new Rule_163();break;
              case 164: rule = new Rule_164();break;
              case 165: rule = new Rule_165();break;
              case 166: rule = new Rule_166();break;
              case 167: rule = new Rule_167();break;
              case 168: rule = new Rule_168();break;
              case 169: rule = new Rule_169();break;
              case 170: rule = new Rule_170();break;
              case 171: rule = new Rule_171();break;
              case 172: rule = new Rule_172();break;
              case 173: rule = new Rule_173();break;
              case 174: rule = new Rule_174();break;
              case 175: rule = new Rule_175();break;
              case 176: rule = new Rule_176();break;
              case 177: rule = new Rule_177();break;
              case 178: rule = new Rule_178();break;
              case 179: rule = new Rule_179();break;
              case 180: rule = new Rule_180();break;
              case 181: rule = new Rule_181();break;
              case 182: rule = new Rule_182();break;
              case 183: rule = new Rule_183();break;
              case 184: rule = new Rule_184();break;
              case 185: rule = new Rule_185();break;
              case 186: rule = new Rule_186();break;
              case 187: rule = new Rule_187();break;
              case 188: rule = new Rule_188();break;
              case 189: rule = new Rule_189();break;
              case 190: rule = new Rule_190();break;
              case 191: rule = new Rule_191();break;
              case 192: rule = new Rule_192();break;
              case 193: rule = new Rule_193();break;
              case 194: rule = new Rule_194();break;
              case 195: rule = new Rule_195();break;
              case 196: rule = new Rule_196();break;
              case 197: rule = new Rule_197();break;
              case 198: rule = new Rule_198();break;
              case 199: rule = new Rule_199();break;
              case 200: rule = new Rule_200();break;
              case 201: rule = new Rule_201();break;
              case 202: rule = new Rule_202();break;
              case 203: rule = new Rule_203();break;
              case 204: rule = new Rule_204();break;
              case 205: rule = new Rule_205();break;
              case 206: rule = new Rule_206();break;
              case 207: rule = new Rule_207();break;
              case 208: rule = new Rule_208();break;
              case 209: rule = new Rule_209();break;
              case 210: rule = new Rule_210();break;
              case 211: rule = new Rule_211();break;
              case 212: rule = new Rule_212();break;
              case 213: rule = new Rule_213();break;
              case 214: rule = new Rule_214();break;
              case 215: rule = new Rule_215();break;
              case 216: rule = new Rule_216();break;
              case 217: rule = new Rule_217();break;
              case 218: rule = new Rule_218();break;
              case 219: rule = new Rule_219();break;
              case 220: rule = new Rule_220();break;
              case 221: rule = new Rule_221();break;
              case 222: rule = new Rule_222();break;
              case 223: rule = new Rule_223();break;
              case 224: rule = new Rule_224();break;
              case 225: rule = new Rule_225();break;
              case 226: rule = new Rule_226();break;
              case 227: rule = new Rule_227();break;
              case 228: rule = new Rule_228();break;
              case 229: rule = new Rule_229();break;
              case 230: rule = new Rule_230();break;
              case 231: rule = new Rule_231();break;
              case 232: rule = new Rule_232();break;
              case 233: rule = new Rule_233();break;
              case 234: rule = new Rule_234();break;
              case 235: rule = new Rule_235();break;
              case 236: rule = new Rule_236();break;
              case 237: rule = new Rule_237();break;
              case 238: rule = new Rule_238();break;
              case 239: rule = new Rule_239();break;
              case 240: rule = new Rule_240();break;
              case 241: rule = new Rule_241();break;
              case 242: rule = new Rule_242();break;
              case 243: rule = new Rule_243();break;
              case 244: rule = new Rule_244();break;
              case 245: rule = new Rule_245();break;
              case 246: rule = new Rule_246();break;
              case 247: rule = new Rule_247();break;
              case 248: rule = new Rule_248();break;
              case 249: rule = new Rule_249();break;
              case 250: rule = new Rule_250();break;
              case 251: rule = new Rule_251();break;
              case 252: rule = new Rule_252();break;
              case 253: rule = new Rule_253();break;
              case 254: rule = new Rule_254();break;
              case 255: rule = new Rule_255();break;
        } 
        return rule;
    }//private ElementaryRuleInterface setRule()
}
