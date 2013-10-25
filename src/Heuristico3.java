import aima.search.framework.HeuristicFunction;


public class Heuristico3 implements HeuristicFunction{

	
	public double getHeuristicValue(Object arg0){
		
		Estado estado = (Estado)arg0;
		return (-estado.P*estado.getBeneficioTotal()+(1-estado.P)*estado.getRetrasos()*30);
		
	}
}
