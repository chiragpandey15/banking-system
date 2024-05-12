package dev.codescreen;

/**
 * Dummy class with a dummy method.
 * Rename this class and method to a name that is more appropriate to your coding test.
 */

public class Error{

    String message;
    String code;
    
    public Error(){}

    public Error(String message,int code){
      
      this.message=message;
      this.code=Integer.toString(code);
    }

  
    public String getCode(){
      return code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
}


