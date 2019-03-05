import java.io.InputStream;
import java.util.Scanner;

class Player extends Thread {
    private InputStream inputStream;
    private int player;
    private Room room;
  
    public Player (InputStream inputStream, int player, Room room) {
      this.inputStream = inputStream;
      this.player = player;
      this.room = room;
    }
  
    public void run() {
  
      Scanner s = new Scanner(this.inputStream);
      while (s.hasNextLine()) {
        room.play(s.nextLine(), this.player);
      }
  
      s.close();
  
    }
 
  }