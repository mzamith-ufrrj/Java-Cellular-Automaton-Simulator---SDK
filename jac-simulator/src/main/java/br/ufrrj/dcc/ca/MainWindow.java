/**
 * 
 */
package br.ufrrj.dcc.ca;
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


import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import br.ufrrj.dcc.ca.models.two.*;


/**
 * 
 */
public class MainWindow  extends JFrame {
		private static final int BOUNDARY = 50;
		private static final int EXIT_CLOSE				= 0;
		private static final int ABOUT          		= 1;
		private static final int ELEMENTARY_CA     		= 2;
		private static final int GOL_RAND               = 3;
		private static final int ITENS                  = 4;
		
	
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
            //--------------------------------------------------------------------------------
			
			//--------------------------------------------------------------------------------

            Dimension tela = Toolkit.getDefaultToolkit().getScreenSize();
            setBounds(BOUNDARY, BOUNDARY, tela.width  - BOUNDARY * 2 , tela.height - BOUNDARY * 2);
            
            mPainel = new JDesktopPane(); 
            mPainel.setBackground(Color.LIGHT_GRAY);

			 
			/*
				
				- The proposed pattener:
			  		Graphic CA is build here and also inserted into mMapCA2D
			  		so that it can be used in menu option. In this moment, the logical
			  		part of CA must be instanced
			 */  
			//#AQUI
			//mMapCA2D = new HashMap<>();
			
			
			//2D Game of life
			//LogicGameOfLife l_gol = new LogicGameOfLife();
			//Internal2DCASIM g_gol = new Internal2DCASIM("Game of Life - rand mode");
			//g_gol.setCAModel(l_gol);
			//mMapCA2D.put(g_gol.getModelName(), g_gol);
			//mMenuHash = new Vector<Integer>();


            this.setContentPane(mPainel);
            this.createMenu();
            this.setVisible(true);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}//public MainWIndow() {
    	

    	
		 
		private void createMenu() {
			mMenuHash = new int[ITENS];
        
			MenuEvent menuEvent = new MenuEvent();
            mMenuBar = new JMenuBar();
            mMenu    = new JMenu("Simulation");
            mMenuBar.add(mMenu);
			mMenuItem = new JMenuItem("1D - Elementary Cellular Automata");;

			mMenuItem.addActionListener(menuEvent);
			mMenuHash[ELEMENTARY_CA] = mMenuItem.hashCode();
            mMenu.add(mMenuItem);
//--------------------------------------------------------------------
            mSubmenu = new JMenu("2D - Cellular Automata");


			mMenuItem = new JMenuItem("Game of life - random");
			mMenuItem.addActionListener(menuEvent);
			mMenuHash[GOL_RAND] = mMenuItem.hashCode();
			mSubmenu.add(mMenuItem);
            mMenu.add(mSubmenu);
			
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
    	
    	
    	/*
    	 * Class listens menu click event
    	 */
        private class MenuEvent implements ActionListener{

            @Override
            public void actionPerformed(ActionEvent e) {
				
				if (e.getSource().hashCode() ==  mMenuHash[ELEMENTARY_CA]){
					System.out.println("CALL Elememtary CA");
					Internal1DCA elementary = new Internal1DCA(mPainel);
					mPainel.add(elementary);
					return;
				}
				
				if (e.getSource().hashCode() ==  mMenuHash[GOL_RAND]){
					LogicGameOfLife l_gol = new LogicGameOfLife();
					Internal2DCASIM g_gol = new Internal2DCASIM("Game of Life - rand mode");
					g_gol.setCAModel(l_gol);
					g_gol.init_window(mPainel);
					mPainel.add(g_gol);
					return;
				}
				
				if (e.getSource().hashCode() ==  mMenuHash[EXIT_CLOSE]){
					System.exit(0);
					return;
				}

				if (e.getSource().hashCode() ==  mMenuHash[ABOUT]){
					JOptionPane.showMessageDialog(null,"ABOUT","TITULO", JOptionPane.INFORMATION_MESSAGE);
					
					return;
				}

            }//end-public void actionPerformed(ActionEvent e) {


        }//end- private class MenuEvent implements ActionListener{	
}//public class MainWIndow  extends JFrame {
