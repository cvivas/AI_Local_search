
public class Peticion implements Cloneable {


	private int centro;
	private int cantidad;
	private int horaLimit;
	
	public Peticion(int centro, int cantidad, int horaLimit){
		super();
		this.setCentro(centro);
		this.setCantidad(cantidad);
		this.setHoraLimit(horaLimit);
	}

	@Override
	public Object clone()  {
		Peticion pet=null;
		try{
			pet=(Peticion)super.clone();
			
			
		}
		catch(Exception e){return -1;}
		
		pet.centro=this.centro;
		pet.cantidad=this.cantidad;
		pet.horaLimit=this.horaLimit;
		
		return pet;
	}

	public int getCentro() {
		return centro;
	}

	public void setCentro(int centro) {
		this.centro = centro;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public int getHoraLimit() {
		return horaLimit;
	}

	public void setHoraLimit(int horaLimit) {
		this.horaLimit = horaLimit;
	}
	
	
	
}
