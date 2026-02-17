package br.ufrrj.dcc.ca.models.two;
import java.util.List;
/*
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.text.NumberFormatter;

import java.text.NumberFormat;
import java.util.ArrayList;
 */
/**
 * @class SimpleCA2DModel
 * @brief Basic implementation of a 2D Cellular Automata model.
 * * This class provides the standard structure for a 2D CA, including 
 * double-buffering lattices (mS0, mS1) and simulation state management.
 */
public class LogicSimpleCA2D implements LogicCA2DInterface {
	/** @brief State indicating the model is not yet defined */
    protected static final int CLASS_STATE_NOT_DEFINED                  = -1;
    
	/** @brief State indicating the model is in its initial stage */
    protected static final int CLASS_STATE_INITIAL                      =  0;
    
	/** @brief State indicating the initial condition is being set */
    protected static final int CLASS_STATE_INITIAL_CONDITION            =  1;
    
	/** @brief State indicating the simulation is performing updates */
    protected static final int CLASS_STATE_UPDATE                       =  2;
    
	/** @brief State indicating the simulation has reached its final condition */
    protected static final int CLASS_STATE_FINAL_CONDITION              =  3;
	
	protected String mBoundary = "";

	protected int nw = -1, 
  				   n = -1, 
			  	  ne = -1, 
				   w = -1, 
				   e = -1, 
				  sw = -1, 
				   s = -1, 
				  se = -1, 
				   c = -1;

    /*
    nw | n | ne
   ----|---|----
    w  | c |  e
   ----|---|----
    sw | s | se
     */	
	
	protected int 	mWidth,			// Number of cells in X axis (width)
				    mHeight,			// Number of cells in Y axis (height)
				    mTimeStep,  	// Current iteration counter
				    mCAState,		// Current lifecycle state of the CA
				    mTotalStates;	// Total number of possible states per cell
	
					/** @brief Lattices for double-buffering (current and next states) */
	protected int[][] mS0, mS1;

	/**
     * @brief Default constructor.
     * Initializes the model with default values and null lattices.
     */	
	public LogicSimpleCA2D() {
		this.mWidth = 0;
		this.mHeight = 0;
		this.mTimeStep = 0;
		this.mS0 = null;
		this.mS1 = null;
		this.mCAState = CLASS_STATE_NOT_DEFINED;
		this.mTotalStates = 0;
			
	
	}

	/*
		A basic settings for 2D CA as Game of Life. Just boundary condition
		and the lattice width and height 
	*/
	@Override
	public boolean settings(){
		return true;
		
	}
	
	/*
	public LogicSimpleCA2D(int w, int h, int s, String b) {
		this.mWidth = w;
		this.mHeight = h;
		this.mTimeStep = 0;
		this.mS0 = new int[this.mHeight][this.mWidth];
		this.mS1 = new int[this.mHeight][this.mWidth];
		this.mCAState = CLASS_STATE_INITIAL;
		this.mTotalStates = s;
		this.mBoundary =  new String(b);
	}//public CellularAutomtaModel() {
	 */
	/**
     * @brief Implementation of the initial condition setup.
     * Defines the starting configuration of the lattice.
     */
	@Override
	public void initialCondition() {
		// TODO Auto-generated method stub
		System.out.println("initial condition");
	}

	/**
     * @brief Updates the simulation to the next time step.
     * Increments the time counter and swaps the buffers (mS0 and mS1)
     * to prepare for the next iteration calculation.
     */
	@Override
	public void update() {
		// TODO Auto-generated method stub
		 this.mTimeStep++;
		 for (int j = 0; j < this.mHeight; j++)
			 for (int i = 0; i < this.mWidth; i++) {
				 int swp = this.mS1[j][i];
				 this.mS1[j][i] = this.mS0[j][i];
				 this.mS0[j][i] = swp;
			 }
		 	
	}

	/**
     * @brief Finalizes the simulation.
     * Used for cleaning up or collecting final data.
     */
	@Override
	public void finalCondition() {
		// TODO Auto-generated method stub
		System.out.println("Final condition");
	}

	/**
     * @brief Returns the current state of the CA model.
     * @return int The internal state of the simulation.
     */
	@Override
	public int getStateCA() 		{ return this.mCAState; }

	/**
     * @brief Returns the width of the lattice.
     * @return int Number of cells in X.
     */
	@Override
	public int getWidth()		 	{ return this.mWidth; }

	/**
     * @brief Returns the height of the lattice.
     * @return int Number of cells in Y.
     */
	@Override
	public int getHeight() 			{ return this.mHeight;}

	/**
     * @brief Gets the state of a specific cell from the active buffer (mS1).
     * @param i X position.
     * @param j Y position.
     * @return int Current state of the cell.
     */
	@Override
	public int getStateCell(int i, int j) {
		return this.mS1[j][i];
	}

	/**
     * @brief Sets the state of a specific cell in the update buffer (mS0).
     * @param i X position.
     * @param j Y position.
     * @param s New state to be assigned.
     */	
	@Override
	public void setStateCell(int i, int j, int s) {
		this.mS0[j][i] = s;
	}
	
	/**
     * @brief Returns the total number of cell states.
     * @return int Total states.
     */
	@Override
	public int getNumberOfStatesCell() { return this.mTotalStates; }

	@Override
	public void setNumberOfStatesCell(int s) { this.mTotalStates = s; }

	@Override
	public String getBoundaryCondition(){ return this.mBoundary; };

	@Override
	public void setBoundaryCondition(String b){ this.mBoundary = new String(b); };
	


	/**
     * @brief Returns the current time step count.
     * @return int Current time step.
     */
	@Override
	public int getTimeStep() { return this.mTimeStep; }
	
	/**
     * @brief Returns models' layers
     * @return Layers by name.
     */
	public List<String> getLayers() { return null; }
	

	/**
     * @brief Returns a log message based on a specific layer.
     * @param i Layer index.
     * @return String Log information.
     */
	public StringBuilder getLogBasedOnLayer(String key) { return null; }

		/**
	 * @brief periodic boundary condition
	 *      
	 * @param i is the axis-x position of lattice
	 * @param j is the axis-y position of lattice
	 */
	protected void periodicBoundary(int i, int j){
		
		if (i + 1 == mWidth && j + 1 == mHeight) {
			se = mS0[0][0]; //ok
		}else if (i + 1 == mWidth){
			se = mS0[j+1][0];
		}else if (j + 1 == mHeight) {
			se = mS0[0][i+1];
		}else {
			se = mS0[j + 1][i + 1]; //ok
		}

		
		if (i - 1 < 0 && j + 1 == mHeight) {
			sw = mS0[0][mWidth - 1]; //ok
		}else if (i - 1 < 0){
			sw = mS0[j + 1][mWidth - 1];
		}else if (j + 1 == mHeight) {
			sw = mS0[0][i-1];
		}else {
			sw = mS0[j + 1][i - 1]; //ok
		}

		
		if (i + 1 == mWidth && j - 1 < 0) {
			ne = mS0[mHeight - 1][0];
		}else if (j - 1 < 0){
			ne = mS0[mHeight - 1][i + 1];
		}else if (i + 1 == mWidth) {
			ne = mS0[j - 1][0];
		}else {
			ne = mS0[j - 1][i + 1];
		}
		
		if (i - 1 < 0 && j - 1 < 0) {
			nw = mS0[mHeight - 1][mWidth - 1];
		}else if (i - 1 < 0){
			nw = mS0[j - 1][mWidth - 1];
		}else if (j - 1 < 0) {
			nw = mS0[mHeight - 1][i - 1];
		}else {
			nw = mS0[j - 1][i - 1];
		}
		
		
		if (i - 1 < 0) {
			w = mS0[j][mWidth - 1];
			e = mS0[j][i + 1];
		}else if (i + 1 == mWidth) {
			w = mS0[j][i - 1];
			e = mS0[j][0];
		}else {
			w = mS0[j][i - 1];
			e = mS0[j][i + 1];
		}	
		
		if (j - 1 < 0) {
			n = mS0[mHeight - 1][i];
			s = mS0[j + 1][i];
		}else if (j + 1 == mHeight) {
			n = mS0[j - 1][i];
			s = mS0[0][i];
		}else {
			n = mS0[j - 1][i];
			s = mS0[j + 1][i];
		}
	}//protected void periodicBoundary(int i, int j){
	
	/**
	 * @brief reflexive boundary condition
	 *      
	 * @param i is the axis-x position of lattice
	 * @param j is the axis-y position of lattice
	 */
	protected void reflexiveBoundary(int i, int j){
		
		if (i + 1 == mWidth && j + 1 == mHeight) {
			se = mS0[j][i];  
		}else if (i + 1 == mWidth){
			se = mS0[j][i];
		}else if (j + 1 == mHeight) {
			se = mS0[j][i];
		}else {
			se = mS0[j + 1][i + 1];  
		}

		
		if (i - 1 < 0 && j + 1 == mHeight) {
			sw = mS0[j][i];  
		}else if (i - 1 < 0){
			sw = mS0[j][i];
		}else if (j + 1 == mHeight) {
			sw = mS0[j][i];
		}else {
			sw = mS0[j + 1][i - 1];  
		}

		
		if (i + 1 == mWidth && j - 1 < 0) {
			ne = mS0[j][i];
		}else if (j - 1 < 0){
			ne = mS0[j][i];
		}else if (i + 1 == mWidth) {
			ne = mS0[j][i];
		}else {
			ne = mS0[j - 1][i + 1];
		}
		
		if (i - 1 < 0 && j - 1 < 0) {
			nw = mS0[j][i];
		}else if (i - 1 < 0){
			nw = mS0[j][i];
		}else if (j - 1 < 0) {
			nw = mS0[j][i];
		}else {
			nw = mS0[j - 1][i - 1];
		}
		
		
		if (i - 1 < 0) {
			w = mS0[j][i + 1];
			e = mS0[j][i + 1];
		}else if (i + 1 == mWidth) {
			w = mS0[j][i - 1];
			e = mS0[j][i - 1];
		}else {
			w = mS0[j][i - 1];
			e = mS0[j][i + 1];
		}	
		
		if (j - 1 < 0) {
			n = mS0[j + 1][i];
			s = mS0[j + 1][i];
		}else if (j + 1 == mHeight) {
			n = mS0[j - 1][i];
			s = mS0[j - 1][i];
		}else {
			n = mS0[j - 1][i];
			s = mS0[j + 1][i];
		}
	}//protected void reflexiveBoundary(int i, int j){
	
	/**
	 * @brief constant boundary condition
	 *      
	 * @param i is the axis-x position of lattice
	 * @param j is the axis-y position of lattice
	 * @param C is constant value, must be one of cell state
	 */
	protected void constantBoundary(int i, int j, int C){
		
		if (i + 1 == mWidth && j + 1 == mHeight) {
			se = C ;//mS0[0][0]; //ok
		}else if (i + 1 == mWidth){
			se = C; //mS0[j+1][0];
		}else if (j + 1 == mHeight) {
			se = C; //mS0[0][i+1];
		}else {
			se = mS0[j + 1][i + 1]; //ok
		}

		
		if (i - 1 < 0 && j + 1 == mHeight) {
			sw = C; //mS0[0][mCellX - 1]; //ok
		}else if (i - 1 < 0){
			sw = C; //mS0[j + 1][mCellX - 1];
		}else if (j + 1 == mHeight) {
			sw = C; //mS0[0][i-1];
		}else {
			sw = mS0[j + 1][i - 1]; //ok
		}

		
		if (i + 1 == mWidth && j - 1 < 0) {
			ne = C; //mS0[mCellY - 1][0];
		}else if (j - 1 < 0){
			ne = C; //mS0[mCellY - 1][i + 1];
		}else if (i + 1 == mWidth) {
			ne = C; //mS0[j - 1][0];
		}else {
			ne = mS0[j - 1][i + 1];
		}
		
		if (i - 1 < 0 && j - 1 < 0) {
			nw = C; //mS0[mCellY - 1][mCellX - 1];
		}else if (i - 1 < 0){
			nw = C; //mS0[j - 1][mCellX - 1];
		}else if (j - 1 < 0) {
			nw = C; //mS0[mCellY - 1][i - 1];
		}else {
			nw = mS0[j - 1][i - 1];
		}
		
		
		if (i - 1 < 0) {
			w = C; //mS0[j][mCellX - 1];
			e = mS0[j][i + 1];
		}else if (i + 1 == mWidth) {
			w = mS0[j][i - 1];
			e = C; //mS0[j][0];
		}else {
			w = mS0[j][i - 1];
			e = mS0[j][i + 1];
		}	
		
		if (j - 1 < 0) {
			n = C; //mS0[mCellY - 1][i];
			s = mS0[j + 1][i];
		}else if (j + 1 == mHeight) {
			n = C; //mS0[j - 1][i];
			s = mS0[0][i];
		}else {
			n = mS0[j - 1][i];
			s = mS0[j + 1][i];
		}
	}//protected void constantBoundary(int i, int j, int C){
}
