package br.ufrrj.dcc.ca.models.logic;

/**
 * @class CellularAutomataInterface
 * @brief Interface used by SimuWindow.java so that it can execute CA instructions.
 * * This interface defines the core contract for any Cellular Automata simulation,
 * covering initialization, state transition, and lattice access.
 */
public interface CellularAutomata2DInterface {
	
	/**
     * @brief Sets the initial condition of the simulation.
     * * This method is responsible for populating the lattice with its starting 
     * states (seed) before the time steps begin.
     */
	public void initialCondition();
	
	/**
     * @brief Transition method to update the system.
     * * Applies the transition rules to all cells in the lattice to move 
     * the simulation to the next time step.
     */
	public void update();

	/**
     * @brief Executes the final instructions of the simulation.
     * * Used for post-processing tasks such as calculating statistics, 
     * generating reports, or saving data to files.
     */
	public void finalCondition();
	

	
	/**
     * @brief Informs the CA code state.
     * * @return int The current part of the simulation lifecycle (e.g., INITIALIZING, RUNNING, FINISHED).
     */
	public int getStateCA();
	
	/**
     * @brief Gets the lattice width.
     * * @return int The number of cells along the x-axis.
     */
	public int getWidth();
	
	/**
     * @brief Gets the lattice height.
     * * @return int The number of cells along the y-axis.
     */
	public int getHeight();
	
	/**
     * @brief Retrieves the state of a specific cell.
     * * @param i The x-coordinate (column) of the cell.
     * @param j The y-coordinate (row) of the cell.
     * @return int The current state value of the cell at (i, j).
     */
	public int getStateCell(int i, int j);
	
	/**
     * @brief Updates the state of a specific cell.
     * @param i The x-coordinate (column) of the cell.
     * @param j The y-coordinate (row) of the cell.
     * @param s The new state value to be assigned.
     */
	public void setStateCell(int i, int j, int s);
	
	/**
     * @brief Gets the total number of possible states.
     * @return int The size of the state set S for this CA.
     */
	public int getNumberOfStatesCell();

 
	public void setNumberOfStatesCell(int s);

     public String getBoundaryCondition();

     public void setBoundaryCondition(String b);
	
	/**
     * @brief Returns the current time step.
     * * @return int The number of iterations executed since the start.
     */
	public int getTimeStep();
	
}
