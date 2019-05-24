package ChatClient;

import java.io.DataOutputStream;

public class Send implements Runnable{
  DataOutputStream outputStream;
  String msg;
  public Send(DataOutputStream outputStream, String msg) {
    this.outputStream = outputStream;
    this.msg = msg;
  }
  public void run() {
    while(true) {
      try {
        outputStream.writeUTF(msg);
      } catch(Exception e) {

      }
    }
  }
}