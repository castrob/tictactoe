import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;

/**
 * Classe TicTacToe representando um jogo da velha na orientação por objetos.
 * Funções:
 *      - Play(x, y, jogador) Realiza uma jogada nas posições x,y para o jogador indicado
 *      - CheckWinner() Verifica a cada rodada se houve um vencedor ou se deu velha!
 *      - toString() Converte o jogo em String para ser mostrado pelo SysOut
 * 
 * @author Ana Letícia Camargos Viana Lara
 * @author João Paulo de Castro Bento Pereira
 * @version 1.0-040319
 */

public class TicTacToe {

    int boardId; /* Id do Jogo */
    char playerA; /* Identificador de cada jogador (x, o) */
    char playerB;
    char board[][]; /* TicTacToe Board */

    /**
     * Construtor Padrão
     * @param char playerA - Identificador do Jogador 1
     * @param car playerB - Ientificador do Jogador 2
     * @param int boardId - Identificador do jogo.
     */

    public TicTacToe(char playerA, char playerB, int boardId){
        this.boardId = boardId;
        this.playerA = playerA;
        this.playerB = playerB;
        this.board = new char[3][3];
        init();
    }

    /**
     * Init é responsável por inicializar o jogo com todas as posições vazias. 
     */

    public void init(){
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                board[i][j] = ' ';
            }
        }
    }

    /**
     * Função play é utilizada para realizar cada jogada de um determinado jogador,
     * esta função não lida com a lógica do servidor. Precisa então ser implementado
     * no lado servidor, a vez de cada jogador.
     * @param int x - Numero da Linha
     * @param int y - Numero da Coluna
     * @param int player - Numero do Jogador (1 ou 2)
     * @return Jogada Válida ou Inválida.
     */
    public boolean play(int x, int y, int player){
        if(((x >= 0 && x <= 2) && (y >= 0 && y <= 2)) && this.board[x][y] == ' '){
            board[x][y] = player == 1 ? playerA : playerB;
            return true;
        }else{
            return false;
        }
    }

    /**
     * Função checkWinner deve ser utilizada após cada jogada,
     * para verificar se houve algum vencedor, ou se o jogo empatou!
     * @return Player 1 Winner!, Player 2 Winner, Tie! ou ""
     */
    public String checkWinner(){
        String result = "";
        if(checkLinesAndColumns(playerA)){
            result =  "Player 1 Winner!";
        }else if (checkLinesAndColumns(playerB)){
            result = "Player 2 Winner!";
        }else if(checkDiagonals(playerA)){
            result = "Player 1 Winner!";
        }else if(checkDiagonals(playerB)){
            result = "Player 2 Winner!";
        }else if(checkTie()){
            return "Tie!";
        }
            return result;
    }

    /**
     * Função auxiliar para verificar se houve vencedor por linhas ou colunas.
     */
    public boolean checkLinesAndColumns(char player){
        int cont = 0;

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(board[i][j] == player){
                    cont++;
                }
            }
            if(cont == 3){
                return true;
            }
            cont = 0;
        }
        
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(board[j][i] == player){
                    cont++;
                }
            }
            if(cont == 3){
                return true;
            }
            cont = 0;
        }

        return false;
    }
    /**
     * Função auxiliar para verificar se houve vencedor por diagonais.
     */
    public boolean checkDiagonals(char player){
        return (this.board[0][0] == player && this.board[1][1] == player && this.board[2][2] == player) ||
        (this.board[0][2] == player && this.board[1][1] == player && this.board[2][1] == player);
    }

    /**
     * Função auxiliar para verificar se houve empate.
     */
    public boolean checkTie(){
        boolean result = true;
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(this.board[i][j] == ' '){
                    result = false;
                    j = 3;
                    i = 3;
                }
            }
        }
        return result;       
    }

    /**
     * função responsável para retornar um equivalente String do jogo.
     * @return jogo em string.
     */
    public String toString(){
        return this.board[0][0] + " | " + this.board[0][1] + " | " + this.board[0][2] + "\n"
               + "----------\n" 
               + this.board[1][0] + " | " + this.board[1][1] + " | " + this.board[1][2] + "\n"
               + "----------\n" 
               + this.board[2][0] + " | " + this.board[2][1] + " | " + this.board[2][2] + "\n";
    }


    /**
     * Main para casos de Testes. comentar no final.
     */
    public static void main(String[] args)throws IOException{
        TicTacToe game = new TicTacToe('x','o', 0);
        BufferedReader p1 = new BufferedReader(new InputStreamReader(System.in));
        String result;
        int x,y;
        int player1 = 1;
        int player2 = 2;
        int current = 1;
        int lastPlayer = 2;
        int temp;
        while((result = game.checkWinner()).equals("")){
            if(current != lastPlayer){
                x = Integer.parseInt(p1.readLine());
                y = Integer.parseInt(p1.readLine());
                while(!game.play(x, y, current)){
                    System.out.println("Invalid Position! Try again!");
                    x = Integer.parseInt(p1.readLine());
                    y = Integer.parseInt(p1.readLine());
                }
                temp = current;
                current = lastPlayer;
                lastPlayer = temp;
            }
            System.out.println(game);
        }
        System.out.println(result);
    }
}