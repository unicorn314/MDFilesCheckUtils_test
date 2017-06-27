package model;

/**
 * cmd命令执行结果.
 * 
 * @author ywx474563 2017年6月27日
 */
public class CmdResult {
  
  private boolean isSuccess;
  
  private String msg;
  
  public CmdResult(boolean isSuccess,String msg){
    this.isSuccess = isSuccess;
    this.msg =msg;
  }

  public boolean isSuccess() {
    return isSuccess;
  }

  public void setSuccess(boolean isSuccess) {
    this.isSuccess = isSuccess;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

}
