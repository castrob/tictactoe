import java.io.InputStream;
import java.io.PrintStream;

/**
 * Classe sala representa uma partida entre dois clients.
 * @author Ana Letícia Camargos Viana Lara
 * @author João Paulo de Castro Bento Pereira
 * @version 1.0-040319
 */
class Room implements Runnable {
    //Objeto TicTacToe representando o jogo da velha
    TicTacToe game;
    Server server;
    //Thread responsável por cada jogador
    Thread player1;
    Thread player2;
    //PrintStream responsável pela comunicação do servidor com o cliente
    PrintStream printStreamPlayer1;
    PrintStream printStreamPlayer2;
    // Ultimo jogador a realizar uma jogada.
    int lastPlayer;
    int id;
  
    /**
     * Construtor Padrão
     * @param InputStream inputStreamPlayer1 - Input do Jogador 1
     * @param InputStream inputStreamPlayer2 - Input do Jogador 2
     * @param PrintStream printSreamPlayer1 - Saida do Jogador 1
     * @param PrintStream printSreamPlayer2 - Saida do Jogador 2
     * @param int id - Numero da Sala
     * @param Server server - Contexto da Sala
     */
    public Room (InputStream inputStreamPlayer1, PrintStream printStreamPlayer1,
     InputStream inputStreamPlayer2, PrintStream printStreamPlayer2, int id, Server server) {
      //Criando uma nova Thread para cada Jogador ( Responsável por lidar com o Input de cada Jogador )
      this.player1 = new Player(inputStreamPlayer1, 1, this);
      this.player2 = new Player(inputStreamPlayer2, 2, this);
      // Fluxo de Saida para cada Jogador ( Onde o Server se Comunica com Eles )
      this.printStreamPlayer1 = printStreamPlayer1;
      this.printStreamPlayer2 = printStreamPlayer2;
      // Configuação Geral da Sala, Jogador 1 Começa Jogando.
      this.lastPlayer = 2;
      this.id = id;
      this.server = server;
      // Inicializando o Jogo e Fazendo Broadcast das instruções.
      this.game = new TicTacToe('x', 'o', id);
      broadcastMessage("Game Started !\n" + game.gameInstructions());
    }
  
    /**
     * Método Run da classe Runnable responsável por iniciar a Thread de cada Jogador
     */
    public void run () {
      player1.start();
      player2.start();
    }
  
    /**
     * Métoo synchronized para controlar a vez de cada jogador
     * Contém também a lógica do jogo em sí.
     * @param String msg - Instrução de cada Jogador por Mensagem
     * @param int player - Jogador que está enviando a mensagem.
     */

    public synchronized void play(String msg, int player){
        String result = "";
        // Mensagens que não são do jogador da rodada, são ignoradas.
        if(lastPlayer != player){
            //Verifica se o jogo ainda não terminou.
            if((result = game.checkWinner()).equals("")){
                String split[] = msg.split(",");
                int x = Integer.parseInt(split[0]);
                int y = Integer.parseInt(split[1]);
                // Realiza a jogada, e caso seja inválida, não muda o jogador.
                if(!game.play(x, y, player)){
                    sendMessage("Invalid Position! Try again!", player);
                }else if(!(result = game.checkWinner()).equals("")){ // Jogada válida, verifica se o jogo terminou.
                    //Faz o broadcast do Resultado e finaliza a Sala.
                    broadcastMessage(result + "\n" + game);
                    endGame();
                }else{
                    //Jogo não acabou, avança a rodada.
                    lastPlayer = player;
                    broadcastMessage("\n" + game);
                    broadcastMessage("Its Player " + ((player) == 1 ? "Two" : "One") + " turn!");
                }
            }
        }else{
            //Jogo já acabou. 
            broadcastMessage(result + "\n" + game);
        }
    }


    /**
     * Método para enviar mensagem para um determinado jogador
     * @param String msg - Mensagem a ser enviada
     * @param int player - Destinatário.
     */
    public void sendMessage(String msg, int player){
        if(player == 1){
            printStreamPlayer1.println(msg);
        }else{
            printStreamPlayer2.println(msg);
        }
    }
  
    /**
     * Método responsável por realizar o broadcast para todos os jogadores da sala.
     * @param String msg - Mensagem a ser enviada.
     */
    public void broadcastMessage(String msg) {
      printStreamPlayer1.println(msg);
      printStreamPlayer2.println(msg);
    }

    /**
     * Método responsável por finalizar a sala.
     */
    public void endGame(){
        printStreamPlayer1.close();
        printStreamPlayer2.close();
        server.endRoom(id);
    }
  }
