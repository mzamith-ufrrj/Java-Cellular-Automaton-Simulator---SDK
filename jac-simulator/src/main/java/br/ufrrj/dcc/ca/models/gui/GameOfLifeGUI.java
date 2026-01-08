package br.ufrrj.dcc.ca.models.gui;

import java.text.NumberFormat;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.text.NumberFormatter;

import br.ufrrj.dcc.ca.gui.SimuWindow;

public class GameOfLifeGUI implements SimpleCA2DGUI{
    
    public GameOfLifeGUI(){
        super();

    }

    @Override
    public void init_component (JDesktopPane p){
        //It is called
        NumberFormat format = NumberFormat.getInstance();
        format.setGroupingUsed(false);
        NumberFormatter formatterx = new NumberFormatter(format);
        formatterx.setValueClass(Integer.class);
        formatterx.setMinimum(0);
        formatterx.setMaximum(Integer.MAX_VALUE);
        formatterx.setAllowsInvalid(false);
        // If you want the value to be committed on each keystroke instead of focus lost
        formatterx.setCommitsOnValidEdit(true);

        NumberFormatter formattery = new NumberFormatter(format);
        formattery.setValueClass(Integer.class);
        formattery.setMinimum(0);
        formattery.setMaximum(Integer.MAX_VALUE);
        formattery.setAllowsInvalid(false);
        // If you want the value to be committed on each keystroke instead of focus lost
        formattery.setCommitsOnValidEdit(true);

        
        JFormattedTextField x_axis = new JFormattedTextField(formatterx);
        JFormattedTextField y_axis = new JFormattedTextField(formattery);
        x_axis.setText("100");
        y_axis.setText("80");
        
        //JTextField x_axis = new JTextField("100");
        //JTextField y_axis = new JTextField("80");
        String s1[] = {"periodic", "reflexive", "constant [0]", "constant [1]"};
        JComboBox<String> combo = new<String>JComboBox<String>(s1);
            
        
        
        JComponent[] inputs = new JComponent[] {
                new JLabel("x-axis lenght"),x_axis,
                new JLabel("y-axis lenght"),y_axis,
                new JLabel("Boundary condition"), combo
        };
        
        
        int result = JOptionPane.showConfirmDialog(null, inputs, "Game of Life - SETUP", JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            System.out.println("Your options:" +
                    x_axis.getText() + ", " +
                    y_axis.getText() + ", " +
                    String.valueOf(combo.getSelectedItem()));
            
            System.out.println("Date: " + Integer.valueOf(x_axis.getText()) + "," + Integer.valueOf(y_axis.getText()) + " -> " +  String.valueOf(combo.getSelectedItem()));
            /*
            SimuWindow s = new SimuWindow(GAME_OF_LIFE, 
                    Integer.valueOf(x_axis.getText()), 
                    Integer.valueOf(y_axis.getText()), 
                    String.valueOf(combo.getSelectedItem()), mPainel);
            
            mPainel.add(s);
            */
        
        } else {
            System.out.println("User canceled / closed the dialog, result = " + result);
        }
    }//public void init (){

    
}
