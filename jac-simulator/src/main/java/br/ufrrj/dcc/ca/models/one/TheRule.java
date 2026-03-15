package br.ufrrj.dcc.ca.models.one;

public class TheRule {
    private int m_i_Rule = -1;
    private String m_s_Rule = null;
    private int mBit0 = 0;
    private int mBit1 = 0;
    public TheRule(int rule){
        m_i_Rule  = rule;
        m_s_Rule = Integer.toBinaryString(rule);
        m_s_Rule = String.format("%8s", m_s_Rule).replace(' ', '0');
        for (int i = 0; i < m_s_Rule.length(); i++){
            if (m_s_Rule.charAt(i) == '0') mBit0++;
            else if (m_s_Rule.charAt(i) == '1') mBit1++;

        }
    }//public TheRule(int rule){
    

    public int apply(int x_l, int x_c, int x_r){
    
        int p = 7 - (x_l * 4 + x_c * 2 + x_r * 1);
        int r = m_s_Rule.charAt(p) & 0x0F; 
        return r;
    }
    public String getBinary(){return new String(m_s_Rule); }

    public String getRuleName(){ return new String("Rule: " + Integer.toString(m_i_Rule));}


  /**
   * Returns the quantity of bits 1 in the binary word.
   * * @return double value in order to avoid casting type. 
   */ 
   public double getB1s(){ return mBit1; }
  /**
   * Returns the quantity of bits 0 in the binary word.
   * * @return double value in order to avoid casting type. 
   */ 
   public double getB0s(){ return mBit0; }
}
