import java.net.InetAddress;

public class Cliente {
	private String nombre;
	private InetAddress ipCliente;
	private int puerto;
	Cliente (String n, InetAddress ipc, int p){
		this.nombre=n;
		this.ipCliente=ipc;
		this.puerto=p;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public InetAddress getIpCliente() {
		return ipCliente;
	}
	public void setIpCliente(InetAddress ipCliente) {
		this.ipCliente = ipCliente;
	}
	public int getPuerto() {
		return puerto;
	}
	public void setPuerto(int puerto) {
		this.puerto = puerto;
	}
	@Override
	public String toString() {
		return "Cliente [nombre=" + nombre + ", ipCliente=" + ipCliente + ", puerto=" + puerto + "]";
	}
	
}
