import java.net.*;
import java.io.*;

/**
 *Esta clase se mantiene escuchando a la espera de peticiones y
 *al recibir alguna de ellas se la pasa a otra clase para que
 *las trate.
 *
 *@author Alba Gamez, Jose Enrique Sanchez
 *@version 0.1
 */

public class WServer {
	private final static int DEFAULT_PORT = 80;
	
	/**
	 *Este metodo se mantiene a la espera de peticiones y cuando
	 *recibe una de ellas se las pasa a otro objeto para que
	 *las atienda.
	 *
	 *@param argv contiene un array de cadenas que se puede ser vacio o
	 *contener en la primera posicion -p y en la segunda numero_puerto.
	 *@since 0.1
	 *@exception NumberFormatException es lanzada en caso de que al pasarle 
	 *el puerto este no se pueda convertir a un numero
	 *@see NumberFormatException
	 */	
	public static void main (String[] argv) throws NumberFormatException {
		ServerSocket serverSocket;
		int port;
		if(argv.length == 0) {
			port = DEFAULT_PORT;
		}else if(argv.length == 2 && argv[0].equals("-p")) {
			port = Integer.parseInt(argv[1]);
		}else {
			System.out.println();
			return;
		}
		
		try	{
			serverSocket = new ServerSocket(port);
			while(true)	{
				/*El motivo principal por el que le pasamos el socket
				 *es para que se encargue de cerrarlo.
				 */
				new Conexion(serverSocket.accept());
			}
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	
}