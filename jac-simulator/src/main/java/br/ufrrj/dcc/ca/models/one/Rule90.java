package br.ufrrj.dcc.ca.models.one;

public class Rule90 implements ElementaryRuleInterface{
    
     public int apply(int x_l, int x_c, int x_r){
        //(x_l and (not x_r)) or  ((not x_l) and x_r)
        return (x_l & (~x_r)) |  (( ~x_l) & x_r);
     }
}
