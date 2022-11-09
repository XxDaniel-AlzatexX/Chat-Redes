import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Cliente {
	private InetAddress ipServidor;
	private int puertoServidor;
	private String nombre;
	private DatagramSocket socketUDP;
	private boolean salirEscucha;
	
	Cliente(String ipS, int ps){
		try {
			this.ipServidor=InetAddress.getByName(ipS);
			this.puertoServidor=ps;
			this.salirEscucha=false;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		iniciarSocket();
	}
	
	public void iniciarSocket(){
		System.out.println("Iniciado el Cliente UDP");
        try {
			this.socketUDP = new DatagramSocket();
			
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public boolean registrarUsuario(String nombre){
		String mensaje = "REG\t"+nombre;
		this.enviarMensaje(mensaje);
		byte bufferRecepcion[]= new byte[1024];
		boolean resultado=false;
		try {
	       DatagramPacket datagramaRecibido = new DatagramPacket(bufferRecepcion, bufferRecepcion.length);
	       this.socketUDP.receive(datagramaRecibido);
	       String mensajeRecibido =new String(datagramaRecibido.getData(),0,datagramaRecibido.getLength());
	       if(mensajeRecibido.compareToIgnoreCase("regok")==0 || mensajeRecibido.compareToIgnoreCase("regupdate")==0) {
	    	   this.nombre=nombre;
	    	   System.out.println("Usuario "+ this.nombre + " Registrado o actualizado correctamente");
	    	   resultado=true;
	       }
	    }
		 catch (IOException e) {
			e.printStackTrace();
		 }
		return resultado;
	}
	
	public void enviarMensaje(String mensaje){
		DatagramPacket datagramaEnviar = new DatagramPacket(mensaje.getBytes(), mensaje.length(), this.ipServidor, this.puertoServidor);
		try {
			this.socketUDP.send(datagramaEnviar);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public InetAddress getIpServidor() {
		return ipServidor;
	}
	public void setIpServidor(InetAddress ipServidor) {
		this.ipServidor = ipServidor;
	}
	public int getPuertoServidor() {
		return puertoServidor;
	}
	public void setPuertoServidor(int puertoServidor) {
		this.puertoServidor = puertoServidor;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public DatagramSocket getSocketUDP() {
		return socketUDP;
	}
	public void setSocketUDP(DatagramSocket socketUDP) {
		this.socketUDP = socketUDP;
	}
}
