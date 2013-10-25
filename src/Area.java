import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.Vector;


public class Area {
	
	
	int numCamiones=60;
	int numCentros=6;
	int maxPeticiones = 1200; //6*10*(2000/100)
	int camionesG, CamionesM, CamionesP;
	Vector<Peticion> peticiones_t;
	Estado est;
	SecureRandom rnd;
	double P;
	
	
	public Area(int estrategia, double P){

		rnd = new SecureRandom();
		this.generador(); //genera las peticiones y los camiones de forma aleatoria. 
		this.P=P;
		this.asignar(estrategia);
		this.getEst().calcularHeuristicos();
		//escribir cosas
		this.escribirFichero();
	}
	
	public int getRandPeticiones(){
		
		return rnd.nextInt(maxPeticiones+100);
//		return 1000;
		
	}
	private void escribirFichero() {
	
		//peticiones
		//camionesg
		//camion M
		//camion P
		//beneficio inicial
		// retraso inicial.
		//beneficio total
		//retraso total
		//P

//		File fichero = new File("salidas.txt");

			try{
			

				BufferedWriter bw = new BufferedWriter(new FileWriter("salidas.txt",true));
				bw.write("-----------------INICIO ---------------------");
				bw.newLine();
				bw.write(this.peticiones_t.size()+"\n");
				bw.write(this.camionesG+"\n");
				bw.write(this.CamionesM+"\n");
				bw.write(this.CamionesP+"\n");
				bw.write(this.getEst().beneficioTotal+"\n");
				bw.write(this.getEst().retrasos+"\n");
				bw.close();
				
			}
			catch (IOException ex){}
		
		
	}


	public void generador() {
		
		//this.camiones=new Vector<Camion>();
		this.peticiones_t= new Vector<Peticion>();
		/*generar camiones*/
		SecureRandom rnd = new SecureRandom();

		this.camionesG=rnd.nextInt(60);
		this.CamionesM=rnd.nextInt(60-this.camionesG);
		this.CamionesP=60-this.camionesG-this.CamionesM;
		int i=0;
		
	    for( i = 0; i < this.getRandPeticiones(); ++i) {
	    	Peticion actual = new Peticion(this.getRandCentro(),this.getRandCantidad(), this.getRandHoraLim());
	    	peticiones_t.add(actual);
	    }
	    System.out.println("Numero de camiones grandes: "+this.camionesG);
	    System.out.println("Numero de camiones medianos "+this.CamionesM);
	    System.out.println("Numero de camiones Pequenos " +this.CamionesP);
	    System.out.println("Numero de peticiones totales "+this.peticiones_t.size());

	}
	
	public void asignar(int estrategia){
		
		
		//variables
		int numPetAsig=0;
		Random r;
		int camP=this.CamionesP;
		int camM=this.CamionesM;
		int camG=this.camionesG;
		int numCamionsAsignados=0;
		/*
		 * 
		 * ASIGNAR TODOS LOS CAMIONES EN LA SOLUCION INICIAL. 
		 * 
		 * 
		 * */
		
		this.est= new Estado(P);//Hay que ver que le pasamos a la creadora !!!
		
		r= new Random();
		while(numCamionsAsignados<60){
			/*ASIGNAMOS LOS CAMIONES DE FORMA ALEATORIA*/
			int cam = r.nextInt(3);
			switch (cam) {
				case 0: if(camP>0){
					this.est.getTablero()[numCamionsAsignados/11][numCamionsAsignados%6]= new Asignacion(500);
					numCamionsAsignados++;
					camP--;
					}
				    break;
				case 1: if(camM>0){
					this.est.getTablero()[numCamionsAsignados/11][numCamionsAsignados%6]= new Asignacion(1000);
					numCamionsAsignados++;
					camM--;
					}
					break;
				case 2: if(camG>0){
					this.est.getTablero()[numCamionsAsignados/11][numCamionsAsignados%6]= new Asignacion(2000);
					numCamionsAsignados++;
					camG--;
					}
					break;
				default: break;			
			}
			
		}
		
		if(estrategia==1){ 
			
			/*
			 * 
			 * ESTRATEGIA SIMPLE
			 * */
			//Asignamos todas las peticiones a los camiones infinitos.
			System.out.println("Estrategia Simple");
			int centro, cantidad, hora;
			for (int j =0; j<6; j++){
				est.tablero[10][j]=new Asignacion(999999);	
			}
			while(numPetAsig != peticiones_t.size()){
				centro= peticiones_t.get(numPetAsig).getCentro();
				cantidad= peticiones_t.get(numPetAsig).getCantidad();
				hora= peticiones_t.get(numPetAsig).getHoraLimit();
				this.est.tablero[10][centro].cargas.add((Peticion)peticiones_t.get(numPetAsig).clone());
				this.est.tablero[10][centro].cargaTotal+=cantidad;
				this.est.tablero[10][centro].horaSalida=hora+8;
				numPetAsig++;
			}
			System.out.println("EN LA 10 0 hay X peticiones: "+this.est.tablero[10][0].getCargas().size());
			
		
		}
		else if (estrategia==2){
			
			/*
			 * ESTRATEGIA COMPLEJA
			 * */
			
			for(int j =0;j<6;j++){
				est.tablero[10][j]=new Asignacion(999999);	
			    }
			
			
			mergeSort(this.peticiones_t);
				while(numPetAsig != peticiones_t.size()){
					asignaPeticion((Peticion)peticiones_t.get(numPetAsig).clone());
					numPetAsig++;
				}
		}
	}
	
	private void mergeSort(Vector<Peticion> peticiones_t2) {
		
		/*
		 * ordena el vector para la solucion compleja
		 * */
	}


	public void asignaPeticion(Peticion p){
		
		boolean asignado=false;
		int i=0;
		int cp=p.getCentro();
		int ct = p.getCantidad();
		
		while (!asignado){
			if(this.est.tablero[i][cp].cargaTotal+ct<=this.est.getTablero()[i][cp].camion){
				
				this.est.tablero[i][cp].cargas.add(p);
				this.est.tablero[i][cp].cargaTotal+=ct;
				this.est.tablero[i][cp].horaSalida=i+8;
				asignado=true;
				
			}
			i++;
		}

	}
	

	
	public int getRandCentro(){
		
		return rnd.nextInt(6);
		
	}
	
	public int getRandCantidad(){ return (rnd.nextInt(5)+1)*100;}
	
	public int getRandHoraLim(){ return (rnd.nextInt(10)+8);}
	
	public int getRandCapacidad(){
		int temp = rnd.nextInt(3);
		switch(temp){
		case 0: return 500; 
		case 1: return 1000; 
		case 2: return 2000; 
		}
	return -1;	
	
	}
	

	public int getNumCamiones() {
		return numCamiones;
	}
	public void setNumCamiones(int numCamiones) {
		this.numCamiones = numCamiones;
	}
	public int getNumCentros() {
		return numCentros;
	}
	public void setNumCentros(int numCentros) {
		this.numCentros = numCentros;
	}
	public int getMaxPeticiones() {
		return maxPeticiones;
	}
	public void setMaxPeticiones(int maxPeticiones) {
		this.maxPeticiones = maxPeticiones;
	}
	public Vector<Peticion> getPeticiones_t() {
		return peticiones_t;
	}
	public void setPeticiones_t(Vector<Peticion> peticiones_t) {
		this.peticiones_t = peticiones_t;
	}
	public Estado getEst() {
		return est;
	}
	public void setEst(Estado est) {
		this.est = est;
	}
	public SecureRandom getRnd() {
		return rnd;
	}
	public void setRnd(SecureRandom rnd) {
		this.rnd = rnd;
	}

	
	
	
	
}
