import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;




public class Estado implements Cloneable{

	SecureRandom rnd;
	Asignacion [][] tablero;//Horas x Centros
	int beneficioTotal;//heuristico 1
	int retrasos; // heuristico 2
	double P;
	//int test;
	
	
	

	@Override
	public Object clone() {
		
		Estado es = null;
		try{
			es=(Estado)super.clone();
			
		}
		catch(Exception e){return -1;}
		es.beneficioTotal=this.beneficioTotal;
		es.retrasos=this.retrasos;
		es.tablero=new Asignacion[11][6];
		es.P = this.P;
		es.tablero=this.copiarMatriz();
		/*
		es.tablero = (Asignacion [][])tablero.clone();
		for (int i =0; i<tablero.length;i++){
			es.tablero[i]= new Asignacion[6];
			es.tablero[i]=(Asignacion [])tablero[i].clone();
		}
			*/
		//es.test=test;
//		for(int i =0;i<66;i++){
//			es.tablero[i%11][i/6]= new Asignacion();
//			es.tablero[i%11][i/6]=(Asignacion)tablero[i/11][i%6].clone();}
		return es;
	}

	public Asignacion[][] copiarMatriz(){
		Asignacion[][] copia= new Asignacion[11][6];
		for (int i = 0; i< 11; i++){
			for (int j=0; j<6; j++){
				
				copia[i][j]= (Asignacion) this.tablero[i][j].clone();
			}
			
			
		}
		
		return copia;
	}
	
	public Estado(){
		super();
		//test=0;
		tablero = new Asignacion[11][6];
		for (int i =0;i<10;i++){
			for(int j =0;j<6;j++){
				tablero[i][j]=new Asignacion();	
			}
		}
//		for(int j =0;j<6;j++){
//			tablero[10][j]=new Asignacion(999999);	
//		}
//		System.out.println("el vector de carga de 0 0 tiene de size: "+tablero[0][0].cargas.size());
		this.beneficioTotal=0;
		this.retrasos=0;
	}
	
	
	public Estado(double P){
		super();
		//test=0;
		tablero = new Asignacion[11][6];
		for (int i =0;i<10;i++){
			for(int j =0;j<6;j++){
				tablero[i][j]=new Asignacion();	
			}
		}
		this.P=P;
//		for(int j =0;j<6;j++){
//			tablero[10][j]=new Asignacion(999999);	
//		}
//		System.out.println("el vector de carga de 0 0 tiene de size: "+tablero[0][0].cargas.size());
		this.beneficioTotal=0;
		this.retrasos=0;
	}
	
	
	/*HEURISTICOS */
	//autodefinido xD en asignacion simpre enemos los valores al dia.
	public void calcularHeuristicos(){
		this.beneficioTotal=0;
		this.retrasos=0;
		this.actualizarHeuristicos();
		for (int i = 0; i<11;i++){
			for(int j = 0;j<6;j++){
				this.beneficioTotal+=this.getTablero()[i][j].beneficio;
				this.retrasos+=this.getTablero()[i][j].tiempoAbs;		
			}	
		}
		
	}
	
	//Actualizo los heristicos en las clases de asignacion para poder tener valores actualizados
	public void actualizarHeuristicos(){
		
		for (int i =0;i<11;i++){
			for (int j = 0; j<6;j++){
			this.getTablero()[i][j].actualizaHeuristicoAsignacion();
			}
		}
	}
		
	
	
	/*OPERADORES*/
	
	/*
	 * 
	 * ****************EN ESTE CASO CREO QUE y1=y2 pues HAN DE ESTAR EN LA MISMA COLUMNA (x lo del centro de produccion)
	 * 
	 * */
	public boolean cambiarPeticion(int x1, int y, int x2, int peticion){
		Peticion p;
//		if(x2 <0 || y2<0){
//			/*Si uno es -1 hemos de crear las paticiones. */
//			p = new Peticion(-1,-1,-1);
//			
//		}
		/*mirar si existe la peticion por intercambiar*/
		boolean b = false;
		p=this.tablero[x1][y].getCargas().get(peticion);
		
		if (x1!=x2 && this.tablero[x2][y].cargaTotal+p.getCantidad()<=this.tablero[x2][y].camion){
			
			this.tablero[x1][y].eliminarPeticion(peticion);
			this.tablero[x2][y].getCargas().add(p);
			this.tablero[x2][y].cargaTotal+=p.getCantidad();
			b=true;
		}
		

		
		this.calcularHeuristicos();
		return b ;
	}
	
	public boolean moverPeticion(int x1, int y, int x2, int peticion1, int peticion2){
		Peticion p=null;
		Peticion p2=null;
		int cargaTotal1;
		int cargaTotal2;

		boolean b = false;

		if (!this.tablero[x1][y].getCargas().isEmpty() && peticion1>=0){
			p=this.tablero[x1][y].getCargas().get(peticion1);
		}
		
		if (!this.tablero[x2][y].getCargas().isEmpty() && peticion2>=0){
			p2=this.tablero[x2][y].getCargas().get(peticion2);
		}
		cargaTotal1=this.tablero[x1][y].cargaTotal;
		cargaTotal2=this.tablero[x2][y].cargaTotal;
		

		if(x1!=x2 && p!=null){
			if (cargaTotal2+p.getCantidad()<=this.tablero[x2][y].camion && p2==null){
				
				//	caso en el que cabe la caja en el destino y no hace falta tocar ninguna peticion
				
				this.tablero[x1][y].eliminarPeticion(peticion1);
				this.tablero[x2][y].getCargas().add(p);
				this.tablero[x2][y].cargaTotal+=p.getCantidad();
				b=true;
			}
			else if(p2!=null && cargaTotal2+p.getCantidad()-p2.getCantidad()<=this.tablero[x2][y].camion && cargaTotal1-p.getCantidad()+p2.getCantidad()<=this.tablero[x1][y].camion ){
				//caso en el que no cabe y hay que probar si puede intercambiarse con la peticion2
				// p2 != nulo, y se puede intercambiar
				
				this.tablero[x1][y].eliminarPeticion(peticion1);
				this.tablero[x2][y].eliminarPeticion(peticion2);
				this.tablero[x1][y].getCargas().add(p2);
				this.tablero[x1][y].cargaTotal+=p2.getCantidad();
				this.tablero[x2][y].getCargas().add(p);
				this.tablero[x2][y].cargaTotal+=p.getCantidad();
				b = true;
				
			}
		}

		
		this.calcularHeuristicos();
		return b ;
	}
	
	public boolean intercambiarCamion(int x1, int y1, int x2, int y2){
		
		int camion1=this.tablero[x1][y1].camion;
		int camion2=this.tablero[x2][y2].camion;
		boolean b = false;
		/*si son de capacidad diferente, se intercambian. */
		if(camion1!= camion2){
			//comprobamos si se pueden intercambiar
			if(this.tablero[x1][y1].cargaTotal<=camion2 && this.tablero[x2][y2].cargaTotal<=camion1 && x1<10 && x2<10 )
			{
				this.tablero[x1][y1].camion=camion2;
				this.tablero[x2][y2].camion=camion1;
				
				b=true;
			}
		}
		
		this.calcularHeuristicos();
		return b;
	}
	
	
	
	public void mostrarSolucio() {

		/*
		 * 
		 * POR HACER!!!!
		 * 
		 * */
		
		System.out.println("");
		System.out.println("----------------- Estado de la solucion --------");
		System.out.println("Beneficios totales: "+this.beneficioTotal);
		System.out.println("Retrasos totales : "+this.retrasos);
		System.out.println("");
		System.out.println("----------------- FIN ESTADO -------------------");
		
		escribirFichero();
	}
	
	

	private void escribirFichero() {
		// 

		//beneficio
		//Retraros
		File fichero = new File("salidas.txt");

		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter("salidas.txt",true));
			bw.write(this.beneficioTotal+"\n");
			bw.write(this.retrasos+"\n");
			bw.write(this.P+"\n");
//			bw.newLine();
//			bw.write("-----------------FIN ---------------------");
//			bw.newLine();
			bw.close();
			
		}
		catch (IOException ex){}

	}

	public Asignacion[][] getTablero() {
		return tablero;
	}

	public void setTablero(Asignacion[][] tablero) {
		this.tablero = tablero;
	}

	public int getBeneficioTotal() {
		return beneficioTotal;
	}

	public void setBeneficioTotal(int beneficioTotal) {
		this.beneficioTotal = beneficioTotal;
	}

	public int getRetrasos() {
		return retrasos;
	}

	public void setRetrasos(int retrasos) {
		this.retrasos = retrasos;
	}


	
	
	
	
	
	
	
	
	
}

