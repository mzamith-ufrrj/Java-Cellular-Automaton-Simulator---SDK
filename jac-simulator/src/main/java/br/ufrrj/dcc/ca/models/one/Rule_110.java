package br.ufrrj.dcc.ca.models.one;
/**
* Implementation of Wolfram's Elementary Cellular Automaton Rule 110.
* <p>
* Rule 110 is a linear elementary cellular automaton where the next state of a cell
* depends on the XOR sum of its left and right neighbors. It is well-known for
* producing complex fractal patterns, specifically the Sierpinski triangle,
* when starting from a single active cell.
* </p>
* * <p>The transition logic follows the Boolean algebraic expression
* * @author Marcelo
* @version 1.0
*/
public class Rule_110 implements ElementaryRuleInterface { 

   /**
     * Returns the formal name of the cellular automaton rule.
     * * @return A string representing the rule name (Rule 110).
     */ 
     public String getRuleName() {return "Rule 110 "; } 

   /** 
     * Applies the transition rule to determine the next state of a cell. 
     * <p> 
     * In Rule 110, the center cell's current state is ignored. The result depends  
     * exclusively on the states of the neighbors. 
     * </p> 
     * * @param x_l The state of the left neighbor. 
     * @param x_c The state of the current (center) cell (ignored in this rule). 
     * @param x_r The state of the right neighbor. 
     * @return The new state of the cell (0 or 1). 
     */ 
     public int apply(int x_l, int x_c, int x_r){ 
        int a = (~x_l) & x_c;
        int b =  x_c & (~x_r); 
        int c = (~x_c) & x_r;
        return a | b | c; 
        
     } 
}
