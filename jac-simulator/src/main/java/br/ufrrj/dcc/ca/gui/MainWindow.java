/**
 * 
 */
package br.ufrrj.dcc.ca.gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.NumberFormatter;



/**
 * 
 */
public class MainWindow  extends JFrame {
		private static final int BOUNDARY = 50;
		private static final int ITENS  = 9;
		
		 
		private static final int EXIT_CLOSE				= 0;
		public static final int GAME_OF_LIFE   			= 1;
	 	private static final int ABOUT          		= 2;
	
	    private JMenuBar mMenuBar = null;
	    private JMenu    mMenu    = null,
	                     mSubmenu = null;
	    private JMenuItem mMenuItem = null;
	    
	    private JDesktopPane mPainel =  null;
	    private int []mMenuHash;
	    
    	public MainWindow() {
            this.setTitle("Cellular automata simulation");
            //this.setExtendedState(JFrame.MAXIMIZED_BOTH); // To maximize a frame
            //int bordas = 50;
            
            Dimension tela = Toolkit.getDefaultToolkit().getScreenSize();
            setBounds(BOUNDARY, BOUNDARY, tela.width  - BOUNDARY * 2 , tela.height - BOUNDARY * 2);
            
            mPainel = new JDesktopPane(); 
            mPainel.setBackground(Color.LIGHT_GRAY);
            this.setContentPane(mPainel);
            this.createMenu();
            this.setVisible(true);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}//public MainWIndow() {
    	

    	
		private void createMenu() {
    		mMenuHash = new int[ITENS];
            //int index = 0;
            
            MenuEvent menuEvent = new MenuEvent();
            mMenuBar = new JMenuBar();
            mMenu    = new JMenu("Simulation");
            mMenuBar.add(mMenu);

//--------------------------------------------------------------------
            mSubmenu = new JMenu("Autômatos Celulares 2D");

            mMenuItem = new JMenuItem("Game of life");
            mMenuItem.addActionListener(menuEvent);
            mMenuHash[GAME_OF_LIFE] = mMenuItem.hashCode();
            mSubmenu.add(mMenuItem);
            
			//This part will be automatic 2026-01-08
			/* 
            mMenuItem = new JMenuItem("Prisoner's Dilemma");
            mMenuItem.addActionListener(menuEvent);
            mMenuHash[PRISONERS_DILEMMA] = mMenuItem.hashCode();
            mSubmenu.add(mMenuItem);
            
            mMenuItem = new JMenuItem("Predator-Prey");
            mMenuItem.addActionListener(menuEvent);
            mMenuHash[PREDATOR_PREY] = mMenuItem.hashCode();
            mSubmenu.add(mMenuItem);
            
            mMenuItem = new JMenuItem("Segregation two-classes");
            mMenuItem.addActionListener(menuEvent);
            mMenuHash[SEGREGATION_2] = mMenuItem.hashCode();
            mSubmenu.add(mMenuItem);
            */
            mMenu.add(mSubmenu);
			
  //--------------------------------------------------------------------
            /*
  			mMenuItem = new JMenuItem("Load native code simulation");
            mMenuItem.addActionListener(menuEvent);
            mMenuHash[NATIVE_SIMULATION] = mMenuItem.hashCode();
            mMenu.add(mSubmenu);
             */
//--------------------------------------------------------------------

            mMenu.addSeparator();
            mMenuItem = new JMenuItem("Exit");
            mMenuItem.addActionListener(menuEvent);
            mMenuHash[EXIT_CLOSE] = mMenuItem.hashCode();
            mMenu.add(mMenuItem);

            mMenu    = new JMenu("About");
            mMenuHash[ABOUT] = mMenuItem.hashCode();
            mMenuBar.add(mMenu);
            
            this.setJMenuBar(mMenuBar);
    	}
    	
    	private void call_game_of_life() {
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
			    
			    SimuWindow s = new SimuWindow(GAME_OF_LIFE, 
			    		Integer.valueOf(x_axis.getText()), 
			    		Integer.valueOf(y_axis.getText()), 
			    		String.valueOf(combo.getSelectedItem()), mPainel);
			  
            	mPainel.add(s);
			
			} else {
			    System.out.println("User canceled / closed the dialog, result = " + result);
			}

    	}

    	
    	/*
    	 * Class listens menu click event
    	 */
        private class MenuEvent implements ActionListener{

            @Override
            public void actionPerformed(ActionEvent e) {
            	JFileChooser fileChooser = null;
            	FileNameExtensionFilter filter = null;
                int option = -1;
                for (int i = 0; i < ITENS; i++){
                    if (e.getSource().hashCode() == mMenuHash[i]){
                        option = i;
                        i = ITENS + 1;
                    }
                }
                switch (option){
                
                case EXIT_CLOSE:System.exit(0);
                case ABOUT:JOptionPane.showMessageDialog(null,"ABOUT","TITULO", JOptionPane.INFORMATION_MESSAGE);break;
                
                case GAME_OF_LIFE:call_game_of_life();break;

                }
            }//end-public void actionPerformed(ActionEvent e) {


        }//end- private class MenuEvent implements ActionListener{	
}//public class MainWIndow  extends JFrame {
