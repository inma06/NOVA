package chat.roomList;

public class MainData {

  private int iv_profile; // 방 프로필 이미지
  private String tv_roomName; // 방이름
  private String tv_content; // 마지막으로 수신된 메시지 내용
  private String tv_date; // 마지막으로 수신된 시간
  private String tv_talkerCount; // 방의 총 인원수

  public int getIv_profile() {
    return iv_profile;
  }

  public void setIv_profile(int iv_profile) {
    this.iv_profile = iv_profile;
  }

  public String getTv_roomName() {
    return tv_roomName;
  }

  public void setTv_roomName(String tv_roomName) {
    this.tv_roomName = tv_roomName;
  }

  public String getTv_content() {
    return tv_content;
  }

  public void setTv_content(String tv_content) {
    this.tv_content = tv_content;
  }

  public String getTv_date() {
    return tv_date;
  }

  public void setTv_date(String tv_date) {
    this.tv_date = tv_date;
  }

  public String getTv_talkerCount() {
    return tv_talkerCount;
  }

  public void setTv_talkerCount(String tv_talkerCount) {
    this.tv_talkerCount = tv_talkerCount;
  }

  public MainData(int iv_profile, String tv_roomName, String tv_content, String tv_date, String tv_talkerCount) {
    this.iv_profile = iv_profile;
    this.tv_roomName = tv_roomName;
    this.tv_content = tv_content;
    this.tv_date = tv_date;
    this.tv_talkerCount = tv_talkerCount;
  }
}

