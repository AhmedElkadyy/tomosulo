
public class ALU {
	String inst="";
    public String id= "";
    boolean Busy=false;
    String Opertions="";
    int Vj=0;
    int Vk=0;
    String Qj="";
    String Qk="";
    boolean execute= false;
    boolean inExecution=false;
    int time=0;
    int stamp=0;
    int clkWB=0;
    public ALU(boolean Busy,String Opertions,int Vj,int Vk,String Qj,String Qk){
        this.Busy=Busy;
        this.Opertions=Opertions;
        this.Vj=Vj;
        this.Vk=Vk;
        this.Qj=Qj;
        this.Qk=Qk;
        this.check();

    }
    
    public void check() {
    	if(this.Qj.equals("") && this.Qk.equals("")) {
            execute= true;  
    	}
   }

}