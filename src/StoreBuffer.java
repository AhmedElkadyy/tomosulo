
public class StoreBuffer {
	String inst="";
    boolean Busy=false;
    String id= "";
    int Address=0;
    int V=0;
    String Q="";
    boolean execute=false;
    boolean inExecution=false;
    int time=0;
    int stamp=0;
    int clkWB=0;
    public StoreBuffer(boolean Busy,int V,String Q, int Address){
        this.Busy=Busy;
        this.Address=Address;
        this.V=V;
        this.Q=Q;
        this.check();
    }
    
    public void check() {
    	 if(this.Q.equals("")) 
             execute=true;    
    }

}
