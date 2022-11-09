import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class RecibirMensajes extends Thread {
	private DatagramSocket socketUDP;
	int mensajesRecibidos;
	boolean salirEscucha;
	 
	RecibirMensajes(){
		 this.mensajesRecibidos=0;	
		 this.salirEscucha=false;
	 }
	
	public DatagramSocket getSocketUDP() {
		return socketUDP;
	}

	public void setSocketUDP(DatagramSocket socketUDP) {
		this.socketUDP = socketUDP;
	}

	public int getMensajesRecibidos() {
		return mensajesRecibidos;
	}

	public void setMensajesRecibidos(int mensajesRecibidos) {
		this.mensajesRecibidos = mensajesRecibidos;
	}

	public boolean isSalirEscucha() {
		return salirEscucha;
	}

	public void setSalirEscucha(boolean salirEscucha) {
		this.salirEscucha = salirEscucha;
	}

	public void run() {
		byte bufferRecepcion[]= new byte[1024];
	    try {
		    while (true && !this.salirEscucha) {
	        	DatagramPacket datagramaRecibido = new DatagramPacket(bufferRecepcion, bufferRecepcion.length);
	            this.socketUDP.receive(datagramaRecibido);
	            String mensaje =new String(datagramaRecibido.getData(),0,datagramaRecibido.getLength());
	            System.out.println("Mensaje Recibido "+ mensaje);
		    }
	    }
		 catch (IOException e) {
			e.printStackTrace();
		 }
	}
}