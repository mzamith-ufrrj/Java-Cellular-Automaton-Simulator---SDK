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
	
	    private JMenuBar mMenuBar = null;
	    private JMenu    mMenu    = null,
	                     mSubmenu = null;
	    private JMenuItem mMenuItem = null;
	    
	    private JDesktopPane mPainel =  null;
	    
		private Vector<Integer> mMenuHash = null;

		private Map<String, Internal2DCASIM> mMapCA2D = null;

		
	    
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
			mMapCA2D = new HashMap<>();
			
			
			//2D Game of life
			LogicGameOfLife l_gol = new LogicGameOfLife();
			Internal2DCASIM g_gol = new Internal2DCASIM("Game of Life - rand mode");
			g_gol.setCAModel(l_gol);
			mMapCA2D.put(g_gol.getModelName(), g_gol);
			mMenuHash = new Vector<Integer>();


            this.setContentPane(mPainel);
            this.createMenu();
            this.setVisible(true);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}//public MainWIndow() {
    	

    	
		 
		private void createMenu() {
			mMenuHash.clear();
            //int index = 0;
            
            MenuEvent menuEvent = new MenuEvent();
            mMenuBar = new JMenuBar();
            mMenu    = new JMenu("Simulation");
            mMenuBar.add(mMenu);
			mMenuItem = new JMenuItem("1D - Elementary Cellular Automata");;

			mMenuItem.addActionListener(menuEvent);
            mMenuHash.add(mMenuItem.hashCode());
            mMenu.add(mMenuItem);
//--------------------------------------------------------------------
            mSubmenu = new JMenu("2D - Cellular Automata");

			
			for (Map.Entry<String, Internal2DCASIM> entry : mMapCA2D.entrySet()) {
				String chave = entry.getKey();
				mMenuItem = new JMenuItem(chave);
				mMenuItem.addActionListener(menuEvent);
				mMenuHash.add(mMenuItem.hashCode());
				mSubmenu.add(mMenuItem);
			}
			
            mMenu.add(mSubmenu);
			
            mMenu.addSeparator();
            mMenuItem = new JMenuItem("Exit");
            mMenuItem.addActionListener(menuEvent);
            mMenuHash.add(mMenuItem.hashCode());
            mMenu.add(mMenuItem);

            mMenu    = new JMenu("About");
            mMenuHash.add(mMenuItem.hashCode());
            mMenuBar.add(mMenu);
            
            this.setJMenuBar(mMenuBar);
    	}
    	
    	
    	/*
    	 * Class listens menu click event
    	 */
        private class MenuEvent implements ActionListener{

            @Override
            public void actionPerformed(ActionEvent e) {
				boolean found = false;
				int i = 0;
				int option = -1;

				if (e.getSource().hashCode() ==  mMenuHash.get(i)){
					System.out.println("CALL Elememtary CA");
					Internal1DCA elementary = new Internal1DCA(mPainel);
					mPainel.add(elementary);
					found = true;
				}
				i++;
				
				//Taking into account there are several different models 2D CA
				while ((i < mMenuHash.size()) && (!found)){
					Integer opt = mMenuHash.get(i);
					if (e.getSource().hashCode() == opt){
                        option = i;
                        found = true;
                    }
					i++;
				}
				if (option == mMenuHash.size() - 2){
					System.exit(0);
					return;
				}

				if (option == mMenuHash.size() - 1){
					JOptionPane.showMessageDialog(null,"ABOUT","TITULO", JOptionPane.INFORMATION_MESSAGE);
					return;
				}

				i = 0;
				for (Map.Entry<String, Internal2DCASIM> entry : mMapCA2D.entrySet()) {
					String chave = entry.getKey();
					Internal2DCASIM valor = entry.getValue();
					if (option - 1 == i){
						valor.init_window(mPainel);
						mPainel.add(valor);
						System.out.println("Found!");
						break;
					}
					
				}
				System.out.println("All your bases are belong to us!");
				/*
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
                
                	case EXIT_CLOSE:
                	case ABOUT:JOptionPane.showMessageDialog(null,"ABOUT","TITULO", JOptionPane.INFORMATION_MESSAGE);break;
                
                	//case GAME_OF_LIFE:call_game_of_life();break;

                }
					 */
            }//end-public void actionPerformed(ActionEvent e) {


        }//end- private class MenuEvent implements ActionListener{	
}//public class MainWIndow  extends JFrame {
