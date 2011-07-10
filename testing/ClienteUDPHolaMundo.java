import java.net.*;
import java.io.*;

public class ClienteUDPHolaMundo {
	private InetAddress servidor = null;
	private int puerto = 0;
	private DatagramSocket datagrama = null;
	private final static String MENSAJE = "Hola mundo";
	private final static int TAM_BUFFER = 100;
	
	public ClienteUDPHolaMundo(InetAddress servidor, int puerto) throws SocketException {
		datagrama = new DatagramSocket();
		this.servidor = servidor;
		this.puerto = puerto;
	}
	
	public void enviarMensaje(String mensaje) throws IOException {
		byte buffer[] = mensaje.getBytes();
		DatagramPacket paquete = new DatagramPacket(
							buffer, buffer.length, servidor, puerto);
		datagrama.send(paquete);
	}
	
	public String recibirMensaje() throws IOException {
		byte buffer[] = new byte[TAM_BUFFER];
		DatagramPacket paquete = new DatagramPacket(buffer, TAM_BUFFER);
		datagrama.receive(paquete);
		return (new String(paquete.getData()));
	}
	
	public void cerrar() {
		datagrama.close();
	}
	
	public static void main(String args[]) throws UnknownHostException, IOException, SocketException {
		InetAddress servidor = null;
		int puerto = 0;
		if(args.length != 2) {
			System.err.println("Número de parámetros insuficiente");
			System.exit(-1);
		}
		servidor = InetAddress.getByName(args[0]);
		puerto = Integer.parseInt(args[1]);
		
		ClienteUDPHolaMundo cliente = new ClienteUDPHolaMundo(servidor, puerto);
		cliente.enviarMensaje(MENSAJE);
		System.out.println("Mensaje recibido: "+ cliente.recibirMensaje() );
		cliente.cerrar();
		
	}
	
}
