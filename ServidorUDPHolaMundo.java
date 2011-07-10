import java.net.*;
import java.io.*;

public class ServidorUDPHolaMundo {
	private DatagramSocket datagrama = null;
	private DatagramPacket paquete = null;
	private int TAM_BUFFER = 100;
	
	public ServidorUDPHolaMundo(InetAddress host, int puerto) throws SocketException {
		datagrama = new DatagramSocket(puerto, host);
	}
	
	public void enviarMensaje() throws IOException {
		if(paquete == null) {
			return;
		}
		byte buffer[] = paquete.getData();
		DatagramPacket paquete2 = new DatagramPacket(
							buffer, buffer.length, paquete.getAddress(), 
							paquete.getPort());
		datagrama.send(paquete);
	}
	
	public String recibirMensaje() throws IOException {
		byte buffer[] = new byte[TAM_BUFFER];
		paquete = new DatagramPacket(buffer, TAM_BUFFER);
		datagrama.receive(paquete);
		return (new String(paquete.getData()));
	}
	
	public void cerrar() {
		datagrama.close();
	}
	
	public static void main(String args[]) throws UnknownHostException, SocketException, IOException {
		InetAddress host = null;
		int puerto = 0;
		if(args.length != 2) {
			System.err.println("Número de parámetros insuficiente");
			System.exit(-1);
		}
		host = InetAddress.getByName(args[0]);
		puerto = Integer.parseInt(args[1]);
		
		ServidorUDPHolaMundo servidor = new ServidorUDPHolaMundo(host, puerto);
		System.out.println("Mensaje recibido: "+ servidor.recibirMensaje() );
		servidor.enviarMensaje();
		servidor.cerrar();
	}
}