import java.util.Vector;


public class Asignacion implements Cloneable {
	public  int camion;
	public Vector<Peticion> cargas;
	int beneficio;
	int tiempoAbs;
	int horaSalida;
	int cargaTotal;

	public Asignacion(){
		super();
		this.camion= 0;
		this.cargas= new Vector<Peticion>();
		this.beneficio=0;
		this.tiempoAbs=0;
		this.horaSalida=0;
		this.cargaTotal=0;
		this.actualizaHeuristicoAsignacion();
	}
	
	public Asignacion(int cam){
		super();
		this.camion= cam;
		this.cargas= new Vector<Peticion>();
		this.beneficio=0;
		this.tiempoAbs=0;
		this.horaSalida=0;
		this.cargaTotal=0;
		this.actualizaHeuristicoAsignacion();
	}
	
	public Asignacion(int camion, Vector<Peticion> peticiones){
		super();
		this.setCamion(camion);
		this.setCargas(peticiones);
		this.beneficio=0;
		this.tiempoAbs=0;
		this.horaSalida=0;
		this.cargaTotal=0;
		this.actualizaHeuristicoAsignacion();
	}

	@Override
	 public Object clone() {
		
		Asignacion as = null;
		try{
		 as=(Asignacion)super.clone();}
		catch(Exception e){
			return -1;
		}
		as.beneficio=this.beneficio;
		as.tiempoAbs=this.tiempoAbs;
		as.horaSalida=this.horaSalida;
		as.cargaTotal=this.cargaTotal;
		
		as.camion=this.camion;
		as.cargas= new Vector<Peticion>();
		//for(Peticion p:this.cargas)as.cargas.add((Peticion)p.clone());
		for (int i=0; i<this.cargas.size(); i++){
			Peticion pet= this.cargas.get(i);
			as.cargas.add(pet);
		}
		return as;
		
	}
	
	public void actualizaHeuristicoAsignacion() {
	
		int i=0;
		this.beneficio=0;
		this.tiempoAbs=0;
		int beneficio_temporal=0;
		int diff_tiempo=0;
		int hora_lim;
		//caso de camiones no infinitos == que se entregan en el mismo dia
		//distingo los dos casos y queda mas largo, pero no hacemos un if dentro de cada for... es mas optimo en teoria. 
		if(this.camion<2001){
			for(i=0;i<this.getCargas().size();i++){
				
				//poner el valor a las cargas en funcion del peso
				beneficio_temporal=this.getCargas().get(i).getCantidad();
				if(beneficio_temporal==500)beneficio_temporal=1000;
				else if(beneficio_temporal>200){beneficio_temporal*=1.5;}
				
				//obtener el retraso
				//si estamos aqui sale hoy, por lo que no contemplamos el caso en que salga al dia siguiente
			
				hora_lim=this.cargas.get(i).getHoraLimit();
				if(this.horaSalida>hora_lim){
					diff_tiempo= this.horaSalida-hora_lim;	
					beneficio_temporal-= (beneficio_temporal*0.2*diff_tiempo);
				}			
				else{					
					diff_tiempo=hora_lim-this.horaSalida;
					
				}
				this.beneficio+=beneficio_temporal;
				this.tiempoAbs+=diff_tiempo;
			}
		}
		else{
			
			//caso en el que repartimos al dia siguiente
			//System.out.println("camiones infinitos");
			
			for(i=0;i<this.getCargas().size();i++){
				//System.out.println("camiones infinitosv2");
				//poner el valor a las cargas en funcion del peso
				beneficio_temporal=this.getCargas().get(i).getCantidad();
				if(beneficio_temporal==500)beneficio_temporal=1000;
				else if(beneficio_temporal>201){beneficio_temporal*=1.5;}
				
				//obtener el retraso
				//si estamos aqui NO sale hoy, por lo que solo contemplamos el dia siguiente
			
				hora_lim=this.cargas.get(i).getHoraLimit();
				
				//contamos hasta las 5 de la tarde
					diff_tiempo= 17-hora_lim;	
					beneficio_temporal-= (beneficio_temporal*0.2*diff_tiempo);
							
				
					
					this.beneficio+=beneficio_temporal;
					/*
					 * SUPONGO QUE EN ESTE CASO LE HE DE SUMAR 1 POR LA HORA EXTRA QUE SE TARDARIA DE UN DIA A OTRO
					 * 
					 * */
					this.tiempoAbs+=diff_tiempo+1;
			}
		
		}
		
	
	}
	
	
	
	public int getBeneficio() {
		return beneficio;
	}

	public void setBeneficio(int beneficio) {
		this.beneficio = beneficio;
	}

	public int getTiempoAbs() {
		return tiempoAbs;
	}

	public void setTiempoAbs(int tiempoAbs) {
		this.tiempoAbs = tiempoAbs;
	}

	public int getHoraSalida() {
		return horaSalida;
	}

	public void setHoraSalida(int horaSalida) {
		this.horaSalida = horaSalida;
	}

	public void eliminarPeticion(int peticion) {
	  if(peticion<this.cargas.size())
		  {
		  this.cargaTotal-=this.cargas.get(peticion).getCantidad();
		  this.cargas.remove(peticion);
		  
		  
		  }
		  
	}
	private void setCamion(int camion) {
		this.camion = camion;
	}

	private void setCargaTotal(int cargaTotal) {
		this.cargaTotal = cargaTotal;
	}

	private int getCargaTotal() {
		return cargaTotal;
	}

	public Vector<Peticion> getCargas() {
		return cargas;
	}

	public void setCargas(Vector<Peticion> cargas) {
		this.cargas = cargas;
	}

	




}