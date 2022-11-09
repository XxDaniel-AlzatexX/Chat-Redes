import java.util.Scanner;

public class ClienteChat {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Cliente cliente  = new Cliente("192.168.0.12",5000);
		RecibirMensajes receptor= new RecibirMensajes();
		
		if (cliente.registrarUsuario("eruiz")) {
			receptor.setSocketUDP(cliente.getSocketUDP());
			receptor.start();
	        Scanner sc = new Scanner(System.in);
	        boolean salir=false;
	        int opc=0;
			do {
				System.out.println("1. Enviar Mensaje\n2.Salir");
				opc= sc.nextInt();
				sc.nextLine();
				String mensajeEnviar="msj\t"+cliente.getNombre()+"\t";
				switch(opc) {
				case 1:{
					System.out.println("Digite Destinatario: ");
					String mensaje=sc.nextLine();
					mensajeEnviar=mensajeEnviar+mensaje+"\t";
					System.out.println("Digite mensaje a enviar: ");
					mensaje=sc.nextLine();
					mensajeEnviar=mensajeEnviar+mensaje;
					cliente.enviarMensaje(mensajeEnviar);
					break;
				}
				case 2: salir=true; break;
				case 3:System.out.println("OpciÃ³n incorecta");
				}
					
			}while(!salir);
			sc.close();
		}
	}
	

}
