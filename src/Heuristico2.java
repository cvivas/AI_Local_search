import aima.search.framework.HeuristicFunction;


public class Heuristico2 implements HeuristicFunction {

	public double getHeuristicValue(Object arg0){
		
		Estado estado = (Estado)arg0;
		return estado.getRetrasos();
		
	}
	
}
