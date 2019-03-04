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

      // adiciona saida do cliente à lista
      PrintStream ps = new PrintStream(cliente.getOutputStream());
      this.clientes.add(ps);
      ps.println("Esperando outro jogador conectar...");

      // aceita segundo cliente
      Socket cliente2 = servidor.accept();
      System.out.println("Nova conexão com o cliente " + cliente2.getInetAddress().getHostAddress());

      // adiciona saida do cliente à lista
      PrintStream ps2 = new PrintStream(cliente2.getOutputStream());
      this.clientes.add(ps2);


      Sala novaSala = new Sala(cliente.getInputStream(), ps ,cliente2.getInputStream(), ps2);
      new Thread(novaSala).start();
      ps.println("Nova sala criada, seja bem-vindo!");
      ps2.println("Nova sala criada, seja bem-vindo!");

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

  PrintStream printStreamPlayer1;
  PrintStream printStreamPlayer2;

  int lastPlayer;

  public Sala (InputStream inputStreamPlayer1, PrintStream printStreamPlayer1,
   InputStream inputStreamPlayer2, PrintStream printStreamPlayer2) {

    this.player1 = new Player(inputStreamPlayer1, 1, this);
    this.player2 = new Player(inputStreamPlayer2, 2, this);

    this.printStreamPlayer1 = printStreamPlayer1;
    this.printStreamPlayer2 = printStreamPlayer2;
  }

  public void run () {
    player1.start();
    player2.start();
  }

  public synchronized void play(String msg, int player){
    if(lastPlayer != player){
      distribuiMensagem(msg + " " + player);
      lastPlayer = player;
    }
  }

  public void distribuiMensagem(String msg) {
    printStreamPlayer1.println(msg);
    printStreamPlayer2.println(msg);
  }
}

class Player extends Thread {
  private InputStream inputStream;
  private int player;
  private Sala sala;

  public Player (InputStream inputStream, int player, Sala sala) {
    this.inputStream = inputStream;
    this.player = player;
    this.sala = sala;
  }

  public void run() {

    Scanner s = new Scanner(this.inputStream);
    while (s.hasNextLine()) {
      sala.play(s.nextLine(), this.player);
    }
    
    s.close();

  }

}