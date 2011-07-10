import java.net.*;
import java.io.*;
import java.util.*;

public class ObtenerIPPublica {
	
	private final static String DIR_CONSULTA_IP = "http://checkip.dyndns.org/";
	/*Posibles sitios de los cuales obtener la IP publica (puede que en cada
	 *uno de ellos haya que extraerla de una forma diferente y que en el futuro
	 *cambie):
	 *
	 *http://whatismyip.com/automation.asp
	 */
	
	public static void main(String args[]) throws Exception {
		InetAddress direccion = InetAddress.getLocalHost();
		System.out.println(direccion);
		System.out.println(direccion.getCanonicalHostName());
		direccion = (new Socket(InetAddress.getByName("google.com"), 80)).getLocalAddress();
		System.out.println(direccion);
		
		/*ninguna de las cosas de arriba funciona, asi que como ya apuntan en
		 *algunos sitios la forma más fácil de hacerlo parece ser
		 *conectandose a alguna página y obteniendo la ip, y para evitar
		 *pedirla cada vez que la necesitas, lo mejor podría ser 
		 *guardarla y pedirla solo cuando sea necesario
		 */
		System.out.println(getIP());
		
	}
	
	public static InetAddress getIP() throws MalformedURLException, IOException, UnknownHostException {
		BufferedReader entrada = new BufferedReader(new InputStreamReader((new URL(DIR_CONSULTA_IP)).openStream()));
		String tmp = extraerIP(entrada.readLine());
		return InetAddress.getByName(tmp);
	}
	
	public static String extraerIP(String texto) {
		StringTokenizer subcadenas = new StringTokenizer(texto, ".");
		int estado = 0;
		String IP = "";
		boolean IPNoEncontrada = true;
		while(IPNoEncontrada && subcadenas.hasMoreTokens()) {
			String tmp = subcadenas.nextToken();
			int num=-1;
			switch(estado) {
				case 0:
					IP = "";
					try {
						if(tmp.length() > 0) {
							num = Integer.parseInt(tmp.substring(tmp.length()-1));
						}
						if(tmp.length() > 1) {
							num = Integer.parseInt(tmp.substring(tmp.length()-2));
						}
						if(tmp.length() > 2) {
							num = Integer.parseInt(tmp.substring(tmp.length()-3));
						}
					}catch(NumberFormatException e){}
					if(num != -1) {
						estado++;
						IP = num+".";
					}
					break;					
				case 1:
					if(tmp.length() >0 && tmp.length() < 4) {
						try {
							num = Integer.parseInt(tmp);
						}catch(NumberFormatException e){}
					}
					if(num != -1) {
						estado++;
						IP += num+".";
					}else {
						estado = 0;
					}
					break;
				case 2:
					if(tmp.length() >0 && tmp.length() < 4) {
						try {
							num = Integer.parseInt(tmp);
						}catch(NumberFormatException e){}
					}
					if(num != -1) {
						estado++;
						IP += num+".";
					}else {
						estado = 0;
					}
					break;					
				case 3:
					try {
						if(tmp.length() > 0) {
							num = Integer.parseInt(tmp.substring(0,1));
						}
						if(tmp.length() > 1) {
							num = Integer.parseInt(tmp.substring(0,2));
						}
						if(tmp.length() > 2) {
							num = Integer.parseInt(tmp.substring(0,3));
						}
					}catch(NumberFormatException e){}
					if(num != -1) {
						IP += num;
						IPNoEncontrada = true;
					}else {
						estado = 0;
					}
					break;
			}
		}
		return IP;
	}
}