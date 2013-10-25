import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;


import aima.search.framework.*;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;


public class main {

	static int step, degree, kannealing;
	static double lambda;
	static double P;
	public static void main(String args[])throws Exception {
	

		/*VARIABLES DEL MAIN*/
		@SuppressWarnings("unused")
		int entrada, estrategia, numCamionP, numCamionM, numCamionG;

		long tempsMillis;
		int tempsExec;
		/*FIN VARIABLES*/
	
		
		P=0;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Bienvenido al sistema de planificacion");
		System.out.println("Escoger la opcion: 1--generar numeros aleatorios, 2-- salir");
		entrada= Integer.parseInt(br.readLine());
		
		while(entrada != 2){
			
			if(entrada==1){//genera numeros aleatorios
				System.out.println("Escoger estrategia para solución inicial:  1--Simple,  2--Compleja");
				estrategia=Integer.parseInt(br.readLine());
				
				System.out.println("Escoger algoritmo (1 - Hill Climbing, 2 - Simulated Annealing)");
				entrada = Integer.parseInt(br.readLine());
				if(entrada == 1){ 
					/*
					 * 
					 * HILLCLIMBING 
					 * 
					 * */
					System.out.println("Escoger heuristico (1 - Maximizar beneficios, 2 - Minimizar tiempos, 3 - Otro??):");
					entrada = Integer.parseInt(br.readLine());
					P = 1;
					if(entrada == 3){
						

						System.out.println("Valor constante P, heuristico 3. p -> [0 - 1]");
						P = Double.parseDouble(br.readLine());
						if((P > 1) || (P < 0)) entrada = -1;
						
					}
					if((entrada != 1) && (entrada != 2) && (entrada != 3)){
						System.out.println("Opcion incorrecta");
						System.out.println("Finalizacion programa");
						break;
					}
					
					/*
					 * ******************************ESTA PARTE SE TIENE QUE MODIFICAR CON EL ESTADO
					 * */
					int index=0;
//					while ( index<10){
						Area area = new Area(estrategia, P);
					
					
						System.out.println("");
						tempsMillis = System.currentTimeMillis();
						exec(area.est, 1, entrada, 0, 0, 0, 0);
						tempsMillis = System.currentTimeMillis() - tempsMillis;
						tempsExec = Math.round(tempsMillis/1000);
						System.out.println("Temps execusió: "+tempsExec);
						System.out.println("");
						escribeTiempoFichero(tempsExec);
						index++;
//					}
//				}
			}
				else	if (entrada==2){
				
				/*Aqui es la parte de Simulated Annealing*/
				/*Hay una p que no se usa*/
				
				System.out.println("Escoger heuristico (1 - Maximizar beneficios, 2 - Minimizar tiempos, 3 - Otro??):");
				entrada = Integer.parseInt(br.readLine());
				P = 1;
				if(entrada == 3){
					System.out.println("Valor constante P, heuristico 3. p -> [0 - 1]");
					P = Double.parseDouble(br.readLine());
					if((P > 1) || (P < 0)) entrada = -1;
				}
				if((entrada != 1) && (entrada != 2) && (entrada != 3)){
					System.out.println("Opció incorrecta");
					System.out.println("Finalització programa");
					break;
				}
				
				System.out.println("Entra parametro step");
				step = Integer.parseInt(br.readLine());
				System.out.println("Entra parametro degree");
				degree = Integer.parseInt(br.readLine());
				System.out.println("Entra parametro kannealing");
				kannealing = Integer.parseInt(br.readLine()); 
				System.out.println("Entra parametro lambda");
				lambda = Double.parseDouble(br.readLine());
				
				
//				step=400;
//				
//				degree=50;
//				kannealing=30;
//				lambda=0.01;
				
				int index2=0;
//				while(index2<10){
					
				Area area = new Area(estrategia,P);

			
							
				System.out.println("");
				tempsMillis = System.currentTimeMillis();
				exec(area.est, 2, entrada, step, degree, kannealing, lambda);
				tempsMillis = System.currentTimeMillis() - tempsMillis;
				tempsExec = Math.round(tempsMillis/1000);
				System.out.println("Temps execusió: "+tempsExec);
				System.out.println("");
				escribeTiempoFichero(tempsExec);
				index2++;
//				}
			
			}
			else{System.out.println("Opcion incorrecta, Finalizacion del programa");break;}
			
			
			}	
			System.out.println("Escoger la opcion: 1--generar numeros aleatorios, 2-- salir");
			entrada = Integer.parseInt(br.readLine());
			
			
			
		}
		System.out.println("HAS SALIDO");
         
         
       
		
		
		
	}

	private static void escribeTiempoFichero(int tempsExec) {
		
		File fichero = new File("salidas.txt");

		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter("salidas.txt",true));
			bw.write(tempsExec+"\n");
			bw.write("parametros simulated:\n");
			//step, degree, kannealing//lambda
			bw.write(step+"\n");
			bw.write(degree+"\n");
			bw.write(kannealing+"\n");
			bw.write(lambda+"\n");
			
			bw.write("-----------------FIN ---------------------");
			bw.newLine();
			bw.close();
			
		}
		catch (IOException ex){}
		
	}

	private static void exec(Estado estado, int algoritmo, int heuristico, int step, int degree,
			int kannealing, double lambda) {
		
		try{
			Estado estatA = new Estado(); 
			
			Problem problem = null;
			HillClimbingSearch searchHC = null;
			SimulatedAnnealingSearch searchSA = null;
			
			if(heuristico == 1){
				problem = new Problem(estado, new Sucesores(), new Goal(), new Heuristico1());
			}else if(heuristico == 2){
				problem = new Problem(estado, new Sucesores(), new Goal(), new Heuristico2());
			}else{
				problem = new Problem(estado, new Sucesores(), new Goal(), new Heuristico3());
			}
			
			if(algoritmo == 1){ 

					searchHC = new HillClimbingSearch();
					SearchAgent agent = new SearchAgent(problem, searchHC);
			}else{
					searchSA = new SimulatedAnnealingSearch(step, degree, kannealing, lambda);
					SearchAgent agent = new SearchAgent(problem, searchSA);
			}

			if (algoritmo == 1) estatA = (Estado) searchHC.getLastSearchState();
			else estatA = (Estado) searchSA.getLastSearchState();
			
			estatA.mostrarSolucio(); // mostrem per pantalla el contingut de l'estat solucio

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}

}
