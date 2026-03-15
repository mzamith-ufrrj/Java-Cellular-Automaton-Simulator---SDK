package br.ufrrj.dcc.ca;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Internal2DCAData extends JInternalFrame{
	private JTextArea mLog = null;
	 
	
	public Internal2DCAData(String layertitle, int x, int y) {
		super(layertitle, true, true, true, false);
		 
		mLog = new JTextArea ();
		mLog.setFont(new Font("Monospaced", Font.PLAIN, 16));
		mLog.setBackground(Color.black);
		mLog.setForeground(Color.yellow);
		mLog.setTabSize(3);
		mLog.setEditable(false);
		//ta.setLineWrap(true);
		//ta.setWrapStyleWord(false);
		//txtQuery.setBounds(10, 10, 365, 45);        
	    this.add(new JScrollPane (mLog));
		mLog.addMouseListener(new MyMouseListener());
		 
		/*
		JTextArea ta = new JTextArea();
		ta.setPreferredSize(new Dimension(858,530));
		ta.setBackground(Color.GREEN);
		ta.setLineWrap(true);
		ta.setWrapStyleWord(false);
		
		this.setLayout(new FlowLayout());
		this.add(ta);
		*/
		
		this.setLocation(x, y);
		this.setSize(800, 600);
		this.setVisible(true);
		this.show();
		
		 
		
	}

    public void setLog(String s) { mLog.setText(new String(s));}
	

	public class MyMouseListener implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			 if (e.getButton() == MouseEvent.BUTTON3){
				JPopupMenu menu = new JPopupMenu();
				JMenuItem item = new JMenuItem("Export to csv");
				item.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						String currentDir = System.getProperty("user.dir");
        				JFileChooser fileChooser = new JFileChooser(currentDir);
						
        				fileChooser.setDialogTitle("Export to CSV format");
						FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files (*.csv)", "CSV");
        				fileChooser.setFileFilter(filter);
        				fileChooser.setAcceptAllFileFilterUsed(false);
						int result = fileChooser.showOpenDialog(null);
						 
						if (result == JFileChooser.APPROVE_OPTION) {
							File CSVFile = fileChooser.getSelectedFile();
							String filePath = CSVFile.getAbsolutePath();
							if (!filePath.toLowerCase().endsWith(".csv")){
								CSVFile = new File(filePath + ".csv");
							} 
							String log = mLog.getText();
							try (PrintWriter writer = new PrintWriter(CSVFile)) {
								writer.print(log);
							} catch (FileNotFoundException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						} else if (result == JFileChooser.CANCEL_OPTION) {
							System.out.println("User cancelled the operation.");
						}
					}
	                    		
	                    
				});// item.addActionListener(new ActionListener() {
				menu.add(item);
				menu.show(e.getComponent(), e.getX(), e.getY());
			 }//end if
		}//end mouseClicked

		@Override
		public void mouseEntered(MouseEvent e) {
			 
		}

		@Override
		public void mouseExited(MouseEvent e) {
			 
		}

		@Override
		public void mousePressed(MouseEvent e) {
			 
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			 
		}
		
	}

}