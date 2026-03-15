package br.ufrrj.dcc.ca;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.ini4j.Wini;


import br.ufrrj.dcc.ca.models.one.Elementary;

public class Internal1DCA extends JInternalFrame{
    private Internal1DCA mPtr = null;
    private JDesktopPane mPainel =  null;
    private int []mMenuHash;
    private JMenuBar mMenuBar = null;
    private JMenuItem mMenuItem = null;
    private JMenu     mMenu     = null;

    private static final int ITENS  		= 8;
    private static final int FRACTAL_D      = 7;
    private static final int EXPORT_INIT    = 6;
    private static final int RUN            = 5;
    private static final int RULE 			= 4;
    private static final int RAND_INIT 		= 0;
	private static final int FILE_INIT      = 1;
	private static final int BOUNDARIRES	= 2;
	private static final int TIME_STEPS     = 3;
    private GUI1DCA mGui1DCA = null;

    private static final String BOUNDARIES[] = {"periodic", "reflexive", "constant [0]", "constant [1]"};

    private int mRule = 90;
    private boolean mIsRand = true;
    
    private String mFileName = "";
    private String mBoundary = "periodic";
    private int mTimestep = 100;
    private int mCALen = 100;
    private double mProb = 0.25f;
    private String mInitialConditionString = "";

    private Elementary mCA = null;
    private static final String TITLE = "Elememtary 1D - Rule: ";
    public Internal1DCA(JDesktopPane p){
        super(TITLE, true, true, true, false);
        this.mPainel = p;
		mPtr = this;
        this.setLocation(30, 30);
        this.setSize(440, 700);
        this.setVisible(true);
        this.show();
		createMenu(); 

        JPanel panel = new JPanel();
        mGui1DCA = new GUI1DCA(this);
        mGui1DCA.setDesktopPane(p);
        panel.add(mGui1DCA);
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Lattice"));
		panel.add(Box.createVerticalStrut(2));
        panel.add(mGui1DCA);
        panel.add(Box.createVerticalStrut(2));

        
        this.getContentPane().add(panel);
        this.setTitle(TITLE + Integer.toString(mRule) + " W(" + Integer.toString(mGui1DCA.getDesktopPane().getAllFrames().length + 1) + ")");
		closeFrameEvent();
        randomInitStateDefault();
    }


    private void createMenu() {
		mMenuHash = new int[ITENS];
         
        
        MenuEvent menuEvent = new MenuEvent();
        mMenuBar = new JMenuBar();
//--------------------------------------------------------------------------------------------------        
        mMenu    = new JMenu("Setup");
        mMenuBar.add(mMenu);
 
        mMenuItem = new JMenuItem("The rule");
        mMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
        mMenuItem.addActionListener(menuEvent);
        mMenuHash[RULE] = mMenuItem.hashCode();
        mMenu.add(mMenuItem);
        this.setJMenuBar(mMenuBar);// index = 3

        mMenuItem = new JMenuItem("Random initial state");
        mMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
        mMenuItem.addActionListener(menuEvent);
        mMenuHash[RAND_INIT] = mMenuItem.hashCode();
        mMenu.add(mMenuItem);
        this.setJMenuBar(mMenuBar);// index = 3

        mMenuItem = new JMenuItem("Boundaries condition");
        mMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0));
        mMenuItem.addActionListener(menuEvent);
        mMenuHash[BOUNDARIRES] = mMenuItem.hashCode();
        mMenu.add(mMenuItem);
        this.setJMenuBar(mMenuBar);// index = 3

        mMenuItem = new JMenuItem("Run");
        mMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        mMenuItem.addActionListener(menuEvent);
        mMenuHash[RUN] = mMenuItem.hashCode();
        mMenu.add(mMenuItem);
        this.setJMenuBar(mMenuBar);// index = 3




        mMenuItem = new JMenuItem("Timesteps");
        mMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0));
        mMenuItem.addActionListener(menuEvent);
        mMenuHash[TIME_STEPS] = mMenuItem.hashCode();
        mMenu.add(mMenuItem);
        this.setJMenuBar(mMenuBar);
      
        mMenuItem = new JMenuItem("Load initial state from file");
        mMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0));
        mMenuItem.addActionListener(menuEvent);
        mMenuHash[FILE_INIT] = mMenuItem.hashCode();
        mMenu.add(mMenuItem);
        this.setJMenuBar(mMenuBar);// index = 3

        mMenuItem = new JMenuItem("Save initial condition to file");
        mMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0));
        mMenuItem.addActionListener(menuEvent);
        mMenuHash[EXPORT_INIT] = mMenuItem.hashCode();
        mMenu.add(mMenuItem);
        this.setJMenuBar(mMenuBar);
     
        mMenuItem = new JMenuItem("Calculate the fractal dimension");
        mMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0));
        mMenuItem.addActionListener(menuEvent);
        mMenuHash[FRACTAL_D] = mMenuItem.hashCode();
        mMenu.add(mMenuItem);
        this.setJMenuBar(mMenuBar);
     
//--------------------------------------------------------------------------------------------------
	}
    
    private void calcFractalDimension(){
        int steps = 128;
        int []t0 = new int[steps*2];
        t0[steps] = 1;
        mCA = new Elementary(this, mRule, t0, "constant [0]", steps);
        this.mGui1DCA.setElementaryCA(mCA);
        mCA.setFractalDimension();
        mCA.start();
        //110 rule
        /*
        int steps = 256;
        int []t0 = new int[steps*2];
        t0[steps*2-2] = 1;
        mCA = new Elementary(this, mRule, t0, "constant [0]", steps);
        this.mGui1DCA.setElementaryCA(mCA);
        mCA.setFractalDimension();
        mCA.start();
         */
        
    }
    private void run(){
        int[] t0;
    
        mCALen = mInitialConditionString.length();
        t0 = new int[mCALen];
        for (int i = 0; i < mCALen; i++){
            char c = mInitialConditionString.charAt(i);
            if ((c >= 0x30) && (c <= 0x39))
                t0[i] = c & 0x0F;
        }
        if (mCA != null) {
    		boolean ret =  mCA.isAlive();
    		mCA.setIsRunningFalse();
    		try {
    			mCA.join();
    		} catch (InterruptedException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		mCA = null;
    		if (ret) return;
    	}//if (mThreadBG != null) {


        mCA = new Elementary(this, mRule, t0, mBoundary, mTimestep);
        this.mGui1DCA.setElementaryCA(mCA);
        if (mIsRand){
            mCA.setWhiteNoise(mProb, true);

        }else{
            mCA.setWhiteNoise(0.0f, false);
        }
        mCA.start();
        
        
    }

    public void finish(){
        this.mGui1DCA.repaint();
       
    }

    private void randomInitStateDefault(){
        

        mInitialConditionString = new String("");
        for (int i = 0; i < mCALen; i++){
            double p = Math.random();
            if (p <= mProb)
                mInitialConditionString += "1";
            else
                mInitialConditionString += "0";
        }

    }
    private void saveInitState(){
        File defaultFile = new File(mFileName);
        String currentDir = System.getProperty("user.dir");
        JFileChooser fileChooser = new JFileChooser(currentDir);
        fileChooser.setSelectedFile(defaultFile);
        fileChooser.setDialogTitle("Type a text file with initial condition");
        int userSelection = fileChooser.showSaveDialog(mPtr);
        String fullPath	= "";
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            fullPath = fileToSave.getAbsolutePath();
            
            System.out.println("Saving at: " + fullPath);
        }
        if (userSelection != JFileChooser.APPROVE_OPTION) return;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fullPath))) {
            writer.write(mInitialConditionString);
            writer.newLine(); // Adiciona uma quebra de linha correta para o SO
        } catch (IOException e) {
            e.printStackTrace();
        }
							
    }

    private void loadInitState(){
        File defaultFile = new File(mFileName);
        String currentDir = System.getProperty("user.dir");
        JFileChooser fileChooser = new JFileChooser(currentDir);
        fileChooser.setSelectedFile(defaultFile);
        fileChooser.setDialogTitle("Select a text file with initial condition");

        FileNameExtensionFilter filter = new FileNameExtensionFilter("ini File (*.ini)", "ini");
        fileChooser.setFileFilter(filter);
        fileChooser.setAcceptAllFileFilterUsed(false);
        
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getAbsolutePath();
            System.out.println("File selected: " + filePath);
            mFileName = new String(filePath);
            try {
                Wini ini = new Wini(new File(mFileName));
            
            
                String type = ini.get("ca", "type");
                String prob = ini.get("ca", "prob");
                mInitialConditionString = ini.get("ca", "ic");
                mBoundary = ini.get("ca", "boundaries");
                String steps = ini.get("ca", "timesteps");
                mTimestep = Integer.parseInt(steps);
                if (type.compareTo("elementary") != 0){
                    JOptionPane.showMessageDialog(mPtr, "The CA is not elementary automata",                   
                    "Error to load initial condition file", JOptionPane.ERROR_MESSAGE);
                }
                mProb = Double.parseDouble(prob);
                if (mProb > 0.0f)mIsRand = true;
                else{
                    mIsRand = false;
                    mProb = 0.0f;
                }
                    

            
            } catch (Exception e) { e.printStackTrace(); } 
        
/*
            try {
                Path path = Paths.get(mFileName);
                mInitialConditionString = Files.readString(path);
                mIsRand = false;
            } catch (IOException e) {
                // Handle the exception (e.g., file not found)
                e.printStackTrace();
            }
 */
        } else if (result == JFileChooser.CANCEL_OPTION) {
            System.out.println("User cancelled the operation.");
        }
    }

    private void setBoundaries(){
        JComboBox<String> combo = new<String>JComboBox<String>(BOUNDARIES);
		JComponent[] inputs = new JComponent[] {new JLabel("Boundary condition"), combo};
		combo.setSelectedItem(mBoundary);
		
		int result = JOptionPane.showConfirmDialog(null, inputs, "CA boundaries condition", JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
            mBoundary = new String(String.valueOf(combo.getSelectedItem()));
        }
    }

    private void setRandom(){
                // 1. Configuração do Comprimento (Inteiro)
        SpinnerNumberModel modelLen = new SpinnerNumberModel(mCALen, 0, Integer.MAX_VALUE, 1);
        JSpinner spinnerLen = new JSpinner(modelLen);
        
        spinnerLen.setEditor(new JSpinner.NumberEditor(spinnerLen, "#"));

        SpinnerNumberModel modelProb = new SpinnerNumberModel(mProb, 0.0, 1.0, 0.001);
        JSpinner spinnerProb = new JSpinner(modelProb);

        JSpinner.NumberEditor editorDouble = new JSpinner.NumberEditor(spinnerProb, "0.000");
        spinnerProb.setEditor(editorDouble);

        JComponent[] inputs = new JComponent[] {
            new JLabel("CA length:"),
            spinnerLen,
            new JLabel("Probability (0.000 a 1.000):"),
            spinnerProb
        };

        int result = JOptionPane.showConfirmDialog(null, inputs, "Setup Cellular Automata", JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            mCALen = (Integer) spinnerLen.getValue();
            mProb = (Double) spinnerProb.getValue();
        }

        randomInitStateDefault();
        
    }

    private void setRule(){
            SpinnerNumberModel model = new SpinnerNumberModel(mRule, 0, 255, 1);
            JSpinner spinner = new JSpinner(model);
            JComponent[] inputs = new JComponent[] {new JLabel("Rule: "), spinner};
            int result = JOptionPane.showConfirmDialog(null, inputs, "CA rule", JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                mRule = (Integer) spinner.getValue();
                //this.setTitle(TITLE + Integer.toString(mRule));
                this.setTitle(TITLE + Integer.toString(mRule) + " W(" + Integer.toString(mGui1DCA.getDesktopPane().getAllFrames().length) + ")");
            }
    }
    private void setTimestep(){
            SpinnerNumberModel model = new SpinnerNumberModel(mTimestep, 0, Integer.MAX_VALUE, 1);
            JSpinner spinner = new JSpinner(model);
            JComponent[] inputs = new JComponent[] {new JLabel("Timesteps: "), spinner};
            int result = JOptionPane.showConfirmDialog(null, inputs, "CA timesteps", JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                mTimestep = (Integer) spinner.getValue();
            }
    }


    private class MenuEvent implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            int option = -1;
            for (int i = 0; i < ITENS; i++){
                if (e.getSource().hashCode() == mMenuHash[i]){
                    option = i;
                    i = ITENS + 1;
                }
            }
            
            //------------------------------------------------

            //-----------------------------------------------
            switch(option){
                case FRACTAL_D:calcFractalDimension();break;
                case RAND_INIT:setRandom();break;
                case FILE_INIT:loadInitState();break;
                case BOUNDARIRES:setBoundaries(); break;
                case TIME_STEPS:setTimestep();break;
                case RULE:setRule();break;
                case RUN:run();break;
                case EXPORT_INIT: saveInitState();break;
            }

        }//end-public void actionPerformed(ActionEvent e) {
    }//private class MenuEvent implements ActionListener{

    private void closeFrameEvent() {
        addInternalFrameListener(new InternalFrameAdapter(){
            public void internalFrameClosing(InternalFrameEvent e) {
                mPtr.dispose();
                //mPainel.remove(mPtr);
            }
        });
    }
    
}
