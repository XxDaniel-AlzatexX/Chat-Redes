import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

public class Servidor {
	private int puerto;
	private DatagramSocket socketUDP;
	ArrayList<Cliente> clientes;
	private boolean salir;
	
	Servidor(int p){
		this.puerto=p;
		this.salir=false;
		iniciarSocket();
		this.clientes= new ArrayList<Cliente>();
	}
	
	 public void iniciarSocket(){
		System.out.println("Iniciado el servidor UDP");
        try {
			this.socketUDP = new DatagramSocket(this.puerto);
			
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	 
	 public void terminarSocket(){
		System.out.println("Terminando el servidor UDP");
        this.socketUDP.close();
	}
	 
	public void escucharMensajes(){	
		byte bufferRecepcion[]= new byte[1024];
	    try {
		    while (true && !this.salir) {
	        	DatagramPacket datagramaRecibido = new DatagramPacket(bufferRecepcion, bufferRecepcion.length);
	            this.socketUDP.receive(datagramaRecibido);
		        this.procesarMensaje(datagramaRecibido);
		    }
	    }
		 catch (IOException e) {
			e.printStackTrace();
		 }
	}
	
	public void procesarMensaje(DatagramPacket datagramaRecibido){
		String mensaje =new String(datagramaRecibido.getData(),0,datagramaRecibido.getLength());
		System.out.println("Mensaje recibido por procesar :"+ mensaje);
		String[] token = mensaje.split("\t");
        if(token[0].compareToIgnoreCase("reg")==0){
	         //Registro de usuario
	    	 Cliente cliente=new Cliente(token[1],datagramaRecibido.getAddress(),datagramaRecibido.getPort());
	    	 int cod = adicionarCliente(cliente);
	    	 //Responde al usuario
	    	 String respuesta;
	    	 switch (cod) {
	    	 case 1: respuesta="REGOK"; break;
	    	 case 2: respuesta="REGDUP";break;
	    	 case 3: respuesta="REGUPDATE";break;
	    	 default: respuesta="REGERROR";break;
	    	 }
	    	 DatagramPacket datagramaEnviar = new DatagramPacket(respuesta.getBytes(), respuesta.length(), datagramaRecibido.getAddress(), datagramaRecibido.getPort());
	    	 try {
				this.socketUDP.send(datagramaEnviar);
			} catch (IOException e) {
				e.printStackTrace();
			}
	    	 
	    	 System.out.println("Usuarios Registrados");
	    	 imprimirClientes();
	     }
	     if(token[0].compareToIgnoreCase("msj")==0){
	         //Mensaje de datos, se espera "msj\tUsuarioOrigen\tUsuarioDestino\tmensaje"
	    	 int posOrigen=buscarCliente(token[1]);
	    	 int posDestino=buscarCliente(token[2]);
	    	 DatagramPacket datagramaEnviar;
	    	 Cliente clienteOrigen=this.clientes.get(posOrigen);
	    	 try {
		         if(posDestino!=-1) {      	 
		        	 Cliente clienteDestino=this.clientes.get(posDestino);

		        		String mensajeReEnviado="MSJ\t"+clienteOrigen.getNombre()+"\t"+clienteDestino.getNombre()+"\t"+token[3]; 
		        		datagramaEnviar = new DatagramPacket(mensajeReEnviado.getBytes(),mensajeReEnviado.length(),clienteDestino.getIpCliente(),clienteDestino.getPuerto());
		        	  	this.socketUDP.send(datagramaEnviar);
		        	  	String respuesta="SENDOK";
		        	  	datagramaEnviar=new DatagramPacket(respuesta.getBytes(),respuesta.length(), clienteOrigen.getIpCliente(),clienteOrigen.getPuerto());
		        	  		this.socketUDP.send(datagramaEnviar);
		        	
					}
		         else {
		        	 String respuesta="NORCPT";
		        	 datagramaEnviar=new DatagramPacket(respuesta.getBytes(),respuesta.length(), clienteOrigen.getIpCliente(),clienteOrigen.getPuerto());
		        	 this.socketUDP.send(datagramaEnviar);
		         }
	    	 } catch (IOException e) {	
					e.printStackTrace();
	         }
	    	 
	     }
	     if(token[0].compareToIgnoreCase("END")==0){
	         //cerrar servidor
	    	 this.terminarSocket();
	    	 this.salir=true;
	     }		
	}
	
	public void imprimirClientes() {
		Iterator<Cliente> it=this.clientes.iterator();
		Cliente clienteAlmacenado=null;
		while(it.hasNext()){
			clienteAlmacenado=it.next();
			System.out.println(clienteAlmacenado.toString());
		}
	}
	
	public int buscarCliente(String nombre) {
		Iterator<Cliente> it=this.clientes.iterator();
		Cliente clienteAlmacenado=null;
		boolean encontro=false;
		int pos=-1;
		while(it.hasNext()&&!encontro){
			clienteAlmacenado=it.next();
			if(clienteAlmacenado.getNombre().compareToIgnoreCase(nombre)==0) {
				encontro=true;
				pos=this.clientes.indexOf(clienteAlmacenado);
			}
		}
		return pos;
	}

	
	public int adicionarCliente(Cliente cliente){
		System.out.println("Registrando ... " + cliente.toString());
		int pos=this.buscarCliente(cliente.getNombre());
		
		if (pos!=-1) {
			Cliente clienteAlmacenado=this.clientes.get(pos);
			if(clienteAlmacenado.getIpCliente().getHostAddress().compareToIgnoreCase(cliente.getIpCliente().getHostAddress())==0) {
				System.out.println("El cliente ya esta en la misma mÃ¡quina");
				//Update
				this.clientes.remove(pos);
				this.clientes.add(pos,cliente);
				return 3;
			}
			else {
				System.out.println("El cliente ya esta registrado en diferente mÃ¡quina");
				//DUP
				return 2;
			}
		}
		else {
			//OK
			System.out.println("El cliente no esta ");
			this.clientes.add(cliente);
			return 1;
		}
	}
	
}