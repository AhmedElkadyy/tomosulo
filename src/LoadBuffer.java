public class LoadBuffer {
	String inst="";
    String id= "";
    int index=0;
    boolean Busy=false;
    int Address=0;
    boolean execute=true;
    boolean inExecution=false;
    int time=0;
    int stamp=0;
    public LoadBuffer(boolean Busy,int Address){
        this.Busy=Busy;
        this.Address=Address;
    }

}
