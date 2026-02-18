package br.ufrrj.dcc.ca;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.NumberFormat;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
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
import javax.swing.event.MenuEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.NumberFormatter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import br.ufrrj.dcc.ca.models.one.Elementary;

public class Internal1DCA extends JInternalFrame{
    private Internal1DCA mPtr = null;
    private JDesktopPane mPainel =  null;
    private int []mMenuHash;
    private JMenuBar mMenuBar = null;
    private JMenuItem mMenuItem = null;
    private JMenu     mMenu     = null;

    private static final int ITENS  		= 6;

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
        mGui1DCA = new GUI1DCA();
        mGui1DCA.setDesktopPane(p);
        panel.add(mGui1DCA);
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Lattice"));
		panel.add(Box.createVerticalStrut(2));
        panel.add(mGui1DCA);
        panel.add(Box.createVerticalStrut(2));

        
        this.getContentPane().add(panel);
        this.setTitle(TITLE + Integer.toString(mRule));
		closeFrameEvent();
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

        mMenuItem = new JMenuItem("Initial state from file");
        mMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0));
        mMenuItem.addActionListener(menuEvent);
        mMenuHash[FILE_INIT] = mMenuItem.hashCode();
        mMenu.add(mMenuItem);
        this.setJMenuBar(mMenuBar);// index = 3

        mMenuItem = new JMenuItem("Run");
        mMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        mMenuItem.addActionListener(menuEvent);
        mMenuHash[RUN] = mMenuItem.hashCode();
        mMenu.add(mMenuItem);
        this.setJMenuBar(mMenuBar);// index = 3


        mMenuItem = new JMenuItem("Boundaries condition");
        mMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0));
        mMenuItem.addActionListener(menuEvent);
        mMenuHash[BOUNDARIRES] = mMenuItem.hashCode();
        mMenu.add(mMenuItem);
        this.setJMenuBar(mMenuBar);// index = 3

        mMenuItem = new JMenuItem("Timesteps");
        mMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0));
        mMenuItem.addActionListener(menuEvent);
        mMenuHash[TIME_STEPS] = mMenuItem.hashCode();
        mMenu.add(mMenuItem);
        this.setJMenuBar(mMenuBar);

     
//--------------------------------------------------------------------------------------------------
	}

    private void run(){
        int[] t0;
        if (mIsRand){
            t0 = new int[mCALen];
            for (int i = 0; i < mCALen; i++){
                double p = Math.random();
                if (p < mProb)
                    t0[i] = 1;
            }
        }else{
            //It is from file, read file text data to array
            if (mInitialConditionString.compareTo("") == 0) return;
            mCALen = mInitialConditionString.length();
            t0 = new int[mCALen];
            for (int i = 0; i < mCALen; i++){
                char c = mInitialConditionString.charAt(i);
                if ((c >= 0x30) && (c <= 0x39))
                    t0[i] = c & 0x0F;
            }
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
        mCA.start();
        
        
    }

    public void finish(){
        this.mGui1DCA.repaint();
       
    }
    private void loadInitState(){
        File defaultFile = new File(mFileName);

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(defaultFile);
        fileChooser.setDialogTitle("Select a text file with initial condition");

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files (*.txt)", "txt");
        fileChooser.setFileFilter(filter);
        fileChooser.setAcceptAllFileFilterUsed(false);
        
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getAbsolutePath();
            System.out.println("File selected: " + filePath);
            mFileName = new String(filePath);

        
            try {
                Path path = Paths.get(mFileName);
                mInitialConditionString = Files.readString(path);
                mIsRand = false;
            } catch (IOException e) {
                // Handle the exception (e.g., file not found)
                e.printStackTrace();
            }

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
    }

    private void setRule(){
            SpinnerNumberModel model = new SpinnerNumberModel(mRule, 0, 255, 1);
            JSpinner spinner = new JSpinner(model);
            JComponent[] inputs = new JComponent[] {new JLabel("Rule: "), spinner};
            int result = JOptionPane.showConfirmDialog(null, inputs, "CA rule", JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                mRule = (Integer) spinner.getValue();
                this.setTitle(TITLE + Integer.toString(mRule));
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
                case RAND_INIT:setRandom();break;
                case FILE_INIT:loadInitState();break;
                case BOUNDARIRES:setBoundaries(); break;
                case TIME_STEPS:setTimestep();break;
                case RULE:setRule();break;
                case RUN:run();break;
                 
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
