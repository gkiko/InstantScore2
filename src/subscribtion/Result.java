package subscribtion;

public class Result {
	private String res, error;
	
	public Result(String res){
		this.res = res;
	}
	
	public Result(){
		
	}
	
	public boolean isValid(){
		return error == null;
	}
	
	public String getErrorMessage(){
		return error;
	}
	
	public String getResult(){
		return res;
	}
	
	public void setErrorMessage(String error){
		this.error = error;
	}
	
	public void setResult(String res){
		this.res = res;
	}
}
