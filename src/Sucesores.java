import java.util.LinkedList;
import java.util.List;

import aima.search.framework.SuccessorFunction;
import aima.search.framework.Successor;


public class Sucesores implements SuccessorFunction{

	public List<Successor> getSuccessors(Object e) {

		LinkedList<Successor> sucesores = new LinkedList<Successor>();
		Estado copia = (Estado)e;
		Successor s;
		
		
		System.out.println("");
		System.out.println("  ESTADO ACTUAL  ");
		System.out.println("    Beneficio Total : "+copia.beneficioTotal);
		System.out.println("    Tiempo Absoluto : "+copia.retrasos);
		System.out.println("");
		
		/*generamos los succesores*/
		
		for (int i = 0; i<11; i++) {
			
			for (int j =0; j<6; j++){
				
				for(int k=0;k<10;k++){
					/*Intercambiamos camiones*/
					for(int l=0;l<6;l++){
						//Estado copia1 = new Estado();
						Estado copia1 = (Estado) copia.clone();
						if(copia1.intercambiarCamion(i, j, k, l)){
							
							s=new Successor(new String(), copia1);
							sucesores.add(s);
							
						}			
					}
		
				} // fin intercambiar camiones
				
				/*Mover Peticiones de los camiones*/
				for(int m=0; m<copia.tablero[i][j].cargas.size();m++){
					
					for(int n=0;n<11;n++){ 
						
						for(int a=-1;a<copia.tablero[n][j].getCargas().size();a++){
							
//							Estado copia2 = new Estado();
							Estado copia2 = (Estado) copia.clone();
						//	copia2.test++;
							if(copia2.moverPeticion(i, j, n, m, a)){
								s=new Successor(new String(), copia2);
								sucesores.add(s);
								
							}
//						 System.out.println("el numero de elementos en las cargas es : "+copia.tablero[n][j].getCargas().size());
							
							//System.out.println("copia de "+n+"  "+j+" vale : "+copia.tablero[n][j].getCargas().size());
						}
//						Estado copia2 = new Estado();
//
//						
//						copia2 = (Estado) copia.clone();
//						if(copia2.cambiarPeticion(i, j, n, m)){
//							
//							s=new Successor(new String(), copia2);
//							sucesores.add(s);
							
//						}	
					}
				}//fin mover peticiones
	
			}
		
		}
		
		
		
		return sucesores;
	}
}
