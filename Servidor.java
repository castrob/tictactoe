import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Servidor {

  public static void main(String[] args) throws IOException {
    // inicia o servidor
    new Servidor(12345).executa();
  }

  private int porta;
  private List<PrintStream> clientes;

  public Servidor (int porta) {
    this.porta = porta;
    this.clientes = new ArrayList<PrintStream>();
  }

  public void executa () throws IOException {
    ServerSocket servidor = new ServerSocket(this.porta);
    System.out.println("Porta 12345 aberta!");

    while (true) {
      // aceita um cliente
      Socket cliente = servidor.accept();
      System.out.println("Nova conexão com o cliente " + cliente.getInetAddress().getHostAddress());

      Socket cliente2 = servidor.accept();
      System.out.println("Nova conexão com o cliente " + cliente2.getInetAddress().getHostAddress());

      // adiciona saida do cliente à lista
      PrintStream ps = new PrintStream(cliente.getOutputStream());
      this.clientes.add(ps);

      PrintStream ps2 = new PrintStream(cliente2.getOutputStream());
      this.clientes.add(ps2);

      Sala novaSala = new Sala(cliente.getInputStream(),cliente2.getInputStream() );
      new Thread(novaSala).start();

      System.out.println("Nova sala criada");

    }
  }

  public void distribuiMensagem(String msg) {
    // envia msg para todo mundo
    for (PrintStream cliente : this.clientes) {
      cliente.println(msg);
    }
  }
}

class Sala implements Runnable {

  Thread player1;
  Thread player2;

  public Sala (InputStream inputStreamPlayer1, InputStream inputStreamPlayer2) {
    this.player1 = new Player(inputStreamPlayer1, 1);
    this.player2 = new Player(inputStreamPlayer2, 2);
  }
  public void run () {
    player1.start();
    player2.start();
  }
}

class Player extends Thread {
  private InputStream inputStream;
  private int player;

  public Player (InputStream inputStream, int player) {
    this.inputStream = inputStream;
    this.player = player;
  }

  public void run() {

  }

}

// class TrataCliente implements Runnable {

//   private InputStream cliente;
//   private InputStream cliente;
//   //private InputStream cliente;
//   private Servidor servidor;

//   public TrataCliente(InputStream cliente, Servidor servidor) {
//     this.cliente = cliente;
//     this.servidor = servidor;
//   }

//   public void run() {
//     // quando chegar uma msg, distribui pra todos
//     Scanner s = new Scanner(this.cliente);
//     while (s.hasNextLine()) {
//       servidor.distribuiMensagem(s.nextLine());
//     }
//     s.close();
//   }
// }