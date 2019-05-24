package ChatClient;

public class Dictionary {

  private String seatNumberTv;
  private int seatStatusIv;
  private String seatStatusTv;

  public String getSeatNumberTv() {
    return seatNumberTv;
  }

  public void setSeatNumberTv(String seatNumberTv) {
    this.seatNumberTv = seatNumberTv;
  }

  public int getSeatStatusIv() {
    return seatStatusIv;
  }

  public void setSeatStatusIv(int seatStatusIv) {
    this.seatStatusIv = seatStatusIv;
  }

  public String getSeatStatusTv() {
    return seatStatusTv;
  }

  public void setSeatStatusTv(String seatStatusTv) {
    this.seatStatusTv = seatStatusTv;
  }

  public Dictionary(String seatNumberTv, int seatStatusIv, String seatStatusTv) {
    this.seatNumberTv = seatNumberTv;
    this.seatStatusIv = seatStatusIv;
    this.seatStatusTv = seatStatusTv;
  }

}
