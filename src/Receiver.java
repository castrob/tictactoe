import java.util.Scanner;
import java.io.InputStream;

/**
 * Recebedor de Mensagens do Servidor para o Cliente
 * @author Ana Letícia Camargos Viana Lara
 * @author João Paulo de Castro Bento Pereira
 */
class Receiver implements Runnable {
    // Stream de mensagens do servidor
    private InputStream server;
  
    /**
     * Construtor padrão
     */
    public Receiver(InputStream server) {
      this.server = server;
    }
  
    /**
     * Método Runnable da thread para receber as mensagens do servidor
     */
    public void run() {
      // recebe msgs do server e imprime na tela
      Scanner s = new Scanner(this.server);
      while (s.hasNextLine()) {
        System.out.println(s.nextLine());
      }
    }
  }