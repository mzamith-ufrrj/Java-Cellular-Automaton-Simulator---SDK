package br.ufrrj.dcc.ca.models.one;

public interface ElementaryRuleInterface {
    
    int apply(int x_l, int x_c, int x_r);

    String getRuleName();

    String getBinary();

    double getB1s();
    double getB0s();
}
