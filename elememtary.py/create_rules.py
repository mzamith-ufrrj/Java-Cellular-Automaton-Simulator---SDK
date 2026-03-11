def int_para_binario_8bits(n) -> str:
    # Garante que o número esteja no intervalo de 0-255 para 8 bits
    if not 0 <= n <= 255:
        return "Valor fora do intervalo de 8 bits"
    
    return f"{n:08b}"

def getValues(value) -> [int, int]:
    a = 0
    b = 0
    
    for s in value:
        if s == '1':
            a = a + 1
        elif s == '0':
            b = b + 1
        else:
            sys.exit(1)
    return a, b


def build_string(rule) -> str:
    binary = int_para_binario_8bits(rule)
    b1s, b0s = getValues(binary)
    content = f"package br.ufrrj.dcc.ca.models.one;\n" \
            f"/**\n"  \
            f"* Implementation of Wolfram's Elementary Cellular Automaton Rule {rule}.\n" \
            f"* <p>\n" \
            f"* Rule {rule} is a linear elementary cellular automaton where the next state of a cell\n" \
            f"* depends on the XOR sum of its left and right neighbors. It is well-known for\n" \
            f"* producing complex fractal patterns, specifically the Sierpinski triangle,\n" \
            f"* when starting from a single active cell.\n" \
            f"* </p>\n" \
            f"* * <p>The transition logic follows the Boolean algebraic expression\n" \
            f"* * @author Marcelo\n" \
            f"* @version 1.0\n" \
            f"*/\n" \
            f"public class Rule_{rule:03d} implements ElementaryRuleInterface {{ \n" \
            f"   /**\n" \
            f"     * Returns the formal name of the cellular automaton rule.\n" \
            f"     * * @return A string representing the rule name (Rule {rule}).\n" \
            f"     */ \n" \
            f"     public String getRuleName() {{return \"Rule {rule} \"; }} \n" \
            f"\n" \
            f"   /** \n" \
            f"     * Applies the transition rule to determine the next state of a cell. \n" \
            f"     * <p> \n" \
            f"     * In Rule {rule}, the center cell's current state is ignored. The result depends  \n" \
            f"     * exclusively on the states of the neighbors. \n" \
            f"     * </p> \n" \
            f"     * * @param x_l The state of the left neighbor. \n" \
            f"     * @param x_c The state of the current (center) cell (ignored in this rule). \n" \
            f"     * @param x_r The state of the right neighbor. \n" \
            f"     * @return The new state of the cell (0 or 1). \n" \
            f"     */ \n" \
            f"     public int apply(int x_l, int x_c, int x_r){{ \n" \
            f"        return 0; \n" \
            f"     }} \n\n" \
            f"  /**\n" \
            f"   * Returns Binary value of rule as string.\n" \
            f"   * * @return binary value of rule. \n" \
            f"   */ \n" \
            f"   public String getBinary(){{ return \"{binary}\"; }}\n" \
            f"  /**\n" \
            f"   * Returns the quantity of bits 1 in the binary word.\n" \
            f"   * * @return double value in order to avoid casting type. \n" \
            f"   */ \n" \
            f"   public double getB1s(){{ return {b1s}; }}\n" \
            f"  /**\n" \
            f"   * Returns the quantity of bits 0 in the binary word.\n" \
            f"   * * @return double value in order to avoid casting type. \n" \
            f"   */ \n" \
            f"   public double getB0s(){{ return {b0s}; }}\n" \
            f"}}\n"

    return content

def java_files():
    for i in range(0, 256):
        content = build_string(i)
        filename = f"Rule_{i:03d}.java"
        print(filename)
        with open(filename, 'w') as file:
            file.write(content)

def java_switch__struct():
    part1 = f"    private ElementaryRuleInterface setRule(){{\n" \
            f"        ElementaryRuleInterface rule = null;\n" \
            f"        switch (mRule) {{"
    print(part1)
    for i in range (0, 256):
        part2 = f"              case {i}: rule = new Rule_{i:03d}();break;" 
        print(part2)
    part3 = f"        }} \n" \
            f"        return rule;\n" \
            f"    }}//private ElementaryRuleInterface setRule()" 
    print(part3)
    

if __name__ == "__main__":
    java_files()
