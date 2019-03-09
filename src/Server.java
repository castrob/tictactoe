import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe Server responsável por aceitar conexões, gerenciar salas e clientes.
 * @author Ana Letícia Camargos Viana Lara
 * @author João Paulo de Castro Bento Pereira
 * @version 1.0-040319
 */
public class Server {

  public static void main(String[] args) throws IOException {
    // inicia o servidor
    new Server(8000).init();
  }

  //Server Port and Client List
  private int port;
  private List<PrintStream> clients;
  private int rooms;

  /**
   * Construtor Padrão
   */
  public Server (int port) {
    this.port = port;
    this.clients = new ArrayList<PrintStream>();
    this.rooms = 1;
  }

  /**
   * Função para inicializar o servidor e aceitar novas conexões.
   * Cria também novas salas com suas respectivas threads.
   */
  public void init () throws IOException {
    ServerSocket server = new ServerSocket(this.port);
    System.out.println("Port :" + this.port + " is open and running!");

    while (true) {
      // aceita um cliente
      Socket client = server.accept();
      System.out.println("New server connection with client: " + client.getInetAddress().getHostAddress());
      

      // adiciona saida do cliente à lista
      PrintStream ps = new PrintStream(client.getOutputStream());
      this.clients.add(ps);
      ps.println("Waiting for another player to connect...");

      // aceita segundo cliente
      Socket client2 = server.accept();
      System.out.println("New server connection with client: " + client2.getInetAddress().getHostAddress());

      // adiciona saida do cliente à lista
      PrintStream ps2 = new PrintStream(client2.getOutputStream());
      this.clients.add(ps2);


      //cria nova sala e a inicializa por thread, envia para cada Cliente seu numero de jogador.
      Room newGameRoom = new Room(client.getInputStream(), ps ,client2.getInputStream(), ps2, rooms, this);
      new Thread(newGameRoom).start();
      ps.println("You are Player One!");
      ps2.println("You are Player Two!");
      System.out.println("New Game Room Created!");
      rooms++;
    }
  }

  /**
   * Método para finalizar a conexão com cada jogador. (Apenas PrintStream)
   */
  public void endRoom(int room){
    int player1, player2;
    player1 = (room * 2) - 2;
    player2 = (room * 2) - 1;
    this.clients.get(player1).close();
    this.clients.get(player2).close();
  }
}