import java.io.IOException;
import java.net.UnknownHostException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Implementação do Cliente realizando apenas conexão com o servidor e enviando/recebendo mensagens.
 * @author Ana Letícia Camargos Viana Lara
 * @author João Paulo de Castro Bento Pereira
 */
public class Client {
  public static void main(String[] args) throws UnknownHostException, IOException {
    // dispara cliente
    new Client("127.0.0.1", 8080).init();
  }

  //Informações de Conexão do Servidor
  private String host;
  private int port;

  /**
   * Construtor Padrão
   * @param String host - ip do servidor
   * @param int port - porta do servidor
   */
  public Client (String host, int port) {
    this.host = host;
    this.port = port;
  }

  /**
   * Método responsável por realizar a conexão com o servidor e enviar as msgs
   */
  public void init() throws UnknownHostException, IOException {
    Socket client = new Socket(this.host, this.port);
    System.out.println("Client connected to server!");

    // thread para receber mensagens do servidor
    Receiver r = new Receiver(client.getInputStream());
    new Thread(r).start();

    // lê msgs do teclado e manda pro servidor
    Scanner teclado = new Scanner(System.in);
    PrintStream saida = new PrintStream(client.getOutputStream());
    while (teclado.hasNextLine()) {
      saida.println(teclado.nextLine());
    }

    //Finalizando Conexões.
    saida.close();
    teclado.close();
    client.close();        
  }
}

