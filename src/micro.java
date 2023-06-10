import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class micro {

    static ALU [] add = new ALU [2] ;
    static ALU [] mul = new ALU [2] ;
    static LoadBuffer [] load = new LoadBuffer [2] ;
    static StoreBuffer [] store = new StoreBuffer [2] ;
    static int [] reg = new int [32];
    static String [] Qi = new String [32];

    static int []Mem = new int[128];
    static String [] instructions = new String [100];

    static int clk=1;
    static int issue=0;

    static int latencyLoad=1;
    static int latencyStore=1;
    static int latencyAdd=2;
    static int latencyMul=10;
    static int latencySub=2;
    static int latencyDiv=10;

    static boolean CDBtaken=false;

    static int n=0;

    static boolean stall=false;
    static String stallR ="";

    public static boolean isFull(Object [] x) {
        for(int i=0; i<x.length;i++) {
            if(x[i]==null)
                return false;
        }

        return true;
    }

    public static boolean isEmpty(){
        for (int i = 0; i < load.length; i++ ) {
            if (load[i]!=null){
                return false;
            }
        }
        for (int i = 0; i < store.length; i++) {
            if (store[i]!=null){
                return false;
            }
        }
        for (int i = 0; i < add.length; i++) {
            if (add[i]!=null){
                return false;
            }
        }
        for (int i = 0; i < mul.length; i++) {
            if (mul[i]!=null){
                return false;
            }
        }
        return true;
    }

    public static void checkStall() {
        if(stallR.equals("load")) {
            for (int i = 0; i < load.length; i++) {
                if (load[i]==null ){
                    stall=false;
                    stallR="";
                }
            }
        }
        else if(stallR.equals("store")) {
            for (int i = 0; i < store.length; i++) {
                if (store[i]==null ){
                    stall=false;
                    stallR="";
                }
            }
        }
        else if(stallR.equals("add")) {
            for (int i = 0; i < add.length; i++) {
                if (add[i]==null ){
                    stall=false;
                    stallR="";
                }
            }
        }
        else if(stallR.equals("mul")) {
            for (int i = 0; i < mul.length; i++) {
                if (mul[i]==null ){
                    stall=false;
                    stallR="";
                }
            }
        }
    }

    public static void issueLoad(String [] splitted ) {
        int regNo;
        if(isFull(load)) {
            stall=true;
            stallR="load";
            issue --;
            System.out.println(instructions[issue]+" is being stalled");
        }
        else {
            String destReg=splitted[1];

            if(destReg.length()==3) {
                String s= destReg.charAt(1)+"" + destReg.charAt(2);
                regNo= Integer.parseInt(s);
            }
            else {
                String s= destReg.charAt(1)+"";
                regNo= Integer.parseInt(s);
            }
            LoadBuffer L= new LoadBuffer(true,Integer.parseInt(splitted[2]));
            L.inst=instructions[issue-1];
            for(int i=0; i<load.length;i++) {
                if(load[i]==null) {
                    L.id="L"+i;
                    load[i]=L;
                    Qi[regNo]="L"+i;
                    break;

                }
            }
            System.out.println(L.inst+ " in "+L.id+" is being issued");

        }

    }
    public static void issueStore(String [] splitted ) {
        int regNo;
        if(isFull(store)) {
            stall=true;
            stallR="store";
            issue --;
            System.out.println(instructions[issue]+" is being stalled");

        }
        else {
            String sourceReg=splitted[1];
            if(sourceReg.length()==3) {
                String s= sourceReg.charAt(1)+"" + sourceReg.charAt(2);
                regNo= Integer.parseInt(s);
            }
            else {
                String s= sourceReg.charAt(1)+"";
                regNo= Integer.parseInt(s);
            }

            String Q="";
            if(Qi[regNo]!=null)
                Q= Qi[regNo];

            StoreBuffer S= new StoreBuffer(true,reg[regNo],Q,Integer.parseInt(splitted[2]));
            S.inst=instructions[issue-1];
            for(int i=0; i<load.length;i++) {
                if(store[i]==null) {
                    S.id="S"+i;
                    store[i] = S;
                    break;
                }
            }
            System.out.println(S.inst+ " in "+S.id+" is being issued");

        }

    }


    public static void issueAdd(String [] splitted ) {
        int regNo;
        int regNo1;
        int regNo2;

        if(isFull(add)) {
            stall=true;
            stallR="add";
            issue --;
            System.out.println(instructions[issue]+" is being stalled");

        }
        else {
            String destReg=splitted[1];
            String sourceReg1=splitted[2];
            String sourceReg2=splitted[3];
            if(destReg.length()==3) {
                String s= destReg.charAt(1)+"" + destReg.charAt(2);
                regNo= Integer.parseInt(s);
            }
            else {
                String s= destReg.charAt(1)+"";
                regNo= Integer.parseInt(s);
            }

            if(sourceReg1.length()==3) {
                String s= sourceReg1.charAt(1)+"" +sourceReg1.charAt(2);
                regNo1= Integer.parseInt(s);
            }
            else {
                String s= sourceReg1.charAt(1)+"";
                regNo1= Integer.parseInt(s);
            }

            if(sourceReg2.length()==3) {
                String s= sourceReg2.charAt(1)+"" +sourceReg2.charAt(2);
                regNo2= Integer.parseInt(s);
            }
            else {
                String s= sourceReg2.charAt(1)+"";
                regNo2= Integer.parseInt(s);
            }

            String Qj="";
            String Qk="";
            if(Qi[regNo1]!=null)
                Qj= Qi[regNo1];
            if(Qi[regNo2]!=null)
                Qk= Qi[regNo2];


            ALU A= new ALU(true,splitted[0],reg[regNo1],reg[regNo2],Qj,Qk);
            A.inst=instructions[issue-1];
            for(int i=0; i<add.length;i++) {
                if(add[i]==null) {
                    A.id="A"+i;
                    add[i]=A;
                    Qi[regNo]="A"+i;
                    break;

                }
            }
            System.out.println(A.inst+ " in "+A.id+" is being issued");

        }


    }

    public static void issueMul(String [] splitted ) {
        int regNo;
        int regNo1;
        int regNo2;

        if(isFull(mul)) {
            stall=true;
            stallR="mul";
            issue --;
            System.out.println(instructions[issue]+" is being stalled");

        }
        else {
            String destReg=splitted[1];
            String sourceReg1=splitted[2];
            String sourceReg2=splitted[3];
            if(destReg.length()==3) {
                String s= destReg.charAt(1)+"" + destReg.charAt(2);
                regNo= Integer.parseInt(s);
            }
            else {
                String s= destReg.charAt(1)+"";
                regNo= Integer.parseInt(s);
            }

            if(sourceReg1.length()==3) {
                String s= sourceReg1.charAt(1)+"" +sourceReg1.charAt(2);
                regNo1= Integer.parseInt(s);
            }
            else {
                String s= sourceReg1.charAt(1)+"";
                regNo1= Integer.parseInt(s);
            }

            if(sourceReg2.length()==3) {
                String s= sourceReg2.charAt(1)+"" +sourceReg2.charAt(2);
                regNo2= Integer.parseInt(s);
            }
            else {
                String s= sourceReg2.charAt(1)+"";
                regNo2= Integer.parseInt(s);
            }

            String Qj="";
            String Qk="";
            if(Qi[regNo1]!=null)
                Qj= Qi[regNo1];
            if(Qi[regNo2]!=null)
                Qk= Qi[regNo2];

            ALU M= new ALU(true,splitted[0],reg[regNo1],reg[regNo2],Qj,Qk);
            M.inst=instructions[issue-1];
            for(int i=0; i<mul.length;i++) {
                if(mul[i]==null) {
                    M.id="M"+i;
                    mul[i]=M;
                    Qi[regNo]="M"+i;
                    break;

                }

            }
            System.out.println(M.inst+ " in "+M.id+" is being issued");

        }
    }
    public static void writeBack(int CDB, String publisher){
        for (int i = 0; i < store.length; i++) {
            if (store[i]!=null && store[i].Q.equals(publisher)){
                store[i].V=CDB;
                store[i].Q="";
                store[i].check();
                store[i].clkWB=clk;
            }
        }
        for (int i = 0; i < add.length; i++) {
            if (add[i]!=null && add[i].Qj.equals(publisher)){
                add[i].Vj=CDB;
                add[i].Qj="";
                add[i].check();
                add[i].clkWB=clk;
            }
            if (add[i]!=null && add[i].Qk.equals(publisher)){
                add[i].Vk=CDB;
                add[i].Qk="";
                add[i].check();
                add[i].clkWB=clk;
            }
        }
        for (int i = 0; i < mul.length; i++) {
            if (mul[i]!=null && mul[i].Qj.equals(publisher)){
                mul[i].Vj=CDB;
                mul[i].Qj="";
                mul[i].check();
                mul[i].clkWB=clk;
            }
            if (mul[i]!=null && mul[i].Qk.equals(publisher)){
                mul[i].Vk=CDB;
                mul[i].Qk="";
                mul[i].check();
                mul[i].clkWB=clk;
            }
        }

        for (int i = 0; i < Qi.length; i++) {
            if (Qi[i]!=null && Qi[i].equals(publisher)){
                reg[i]=CDB;
                Qi[i]=null;
            }
        }


    }


    public static void executeLoad(LoadBuffer lb ){
        if(lb.inExecution==false) {
            lb.time= latencyLoad+clk;
            lb.inExecution=true;
            lb.stamp=clk+1;
        }
        if(clk==lb.stamp)
            System.out.println(lb.inst+" in " +lb.id+" has started executing");
        if(clk==(lb.time)) {
            System.out.println(lb.inst+" in " +lb.id+" has finished executing");

        }


        if (clk>=(lb.time)+1 && CDBtaken==false){
            CDBtaken=true;
            writeBack(Mem[lb.Address],lb.id);
            load[Integer.parseInt(lb.id.charAt(1)+"")]=null;
            System.out.println(lb.inst+" in " +lb.id+" is writing back");

        }
    }


    public static void executeStore(StoreBuffer sb ) {
        if(sb.inExecution==false) {
            sb.time= latencyStore+clk;
            sb.inExecution=true;
            sb.stamp=clk+1;
        }
        if(clk==sb.stamp)
            System.out.println(sb.inst+" in " +sb.id+" has started executing");

        if (clk == sb.time) {
            Mem[sb.Address] = sb.V;
            System.out.println(sb.inst+" in " +sb.id + " has finished executing");
            store[Integer.parseInt(sb.id.charAt(1)+"")]=null;
        }
    }

    public static void executeAdd(ALU a ) {

        if ((a.Opertions).equals("ADD.D")) {
            if (a.inExecution == false) {
                a.time = latencyAdd + clk;
                a.inExecution = true;
                a.stamp=clk+1;
            }
            if(clk==a.stamp)
                System.out.println(a.inst+" in " +a.id+" has started executing");
            if(clk==(a.time)) {
                System.out.println(a.inst+" in " +a.id+" has finished executing");

            }
            if (clk >= (a.time) + 1 && CDBtaken == false) {
                CDBtaken = true;
                writeBack(a.Vj + a.Vk, a.id);
                add[Integer.parseInt(a.id.charAt(1) + "")] = null;
                System.out.println(a.inst+" in " +a.id+" is writing back");

            }
        }

        if (a.Opertions.equals("SUB.D")) {
            if (a.inExecution == false) {
                a.time = latencySub + clk;
                a.inExecution = true;
                a.stamp=clk+1;
            }
            if(clk==a.stamp)
                System.out.println(a.inst+" in " +a.id+" has started executing");
            if(clk==(a.time)) {
                System.out.println(a.inst+" in " +a.id+" has finished executing");

            }
            if (clk >= (a.time) + 1 && CDBtaken == false) {
                CDBtaken = true;
                writeBack(a.Vj - a.Vk, a.id);
                add[Integer.parseInt(a.id.charAt(1) + "")] = null;
                System.out.println(a.inst+" in " +a.id+" is writing back");

            }
        }

    }

    public static void executeMul(ALU a ) {

        if (a.Opertions.equals("MUL.D")) {
            if (a.inExecution == false) {
                a.time = latencyMul + clk;
                a.inExecution = true;
                a.stamp=clk+1;
            }
            if(clk==a.stamp)
                System.out.println(a.inst+" in " +a.id+" has started executing");
            if(clk==(a.time)) {
                System.out.println(a.inst+" in " +a.id+" has finished executing");

            }
            if (clk >= (a.time) + 1 && CDBtaken == false) {
                CDBtaken = true;
                writeBack(a.Vj * a.Vk, a.id);
                mul[Integer.parseInt(a.id.charAt(1) + "")] = null;
                System.out.println(a.inst+" in " +a.id+" is writing back");

            }
        }

        if (a.Opertions.equals("DIV.D")) {
            if (a.inExecution == false) {
                a.time = latencyDiv + clk;
                a.inExecution = true;
                a.stamp=clk+1;

            }
            if(clk==a.stamp)
                System.out.println(a.inst+" in " +a.id+" has started executing");
            if(clk==(a.time)) {
                System.out.println(a.inst+" in " +a.id+" has finished executing");
            }
            if (clk >= (a.time) + 1 && CDBtaken == false) {
                CDBtaken = true;
                writeBack(a.Vj / a.Vk, a.id);
                mul[Integer.parseInt(a.id.charAt(1) + "")] = null;
                System.out.println(a.inst+" in " +a.id+" is writing back");

            }
        }

    }



    public static void Tomasulo() {

        do {
            System.out.println("At clock cycle: "+clk);
            if(stall==false && issue<n) {
                String [] splitted = (instructions[issue]).split(" ");
                issue ++;
                String op = splitted[0];
                if(op.equals("L.D")) {
                    issueLoad(splitted);
                }
                else if(op.equals("S.D")) {
                    issueStore(splitted);
                }
                else if(op.equals("ADD.D") || op.equals("SUB.D")) {
                    issueAdd(splitted);
                }
                else if(op.equals("MUL.D") || op.equals("DIV.D")) {
                    issueMul(splitted);
                }
            }
            for (int i = 0; i < load.length; i++) {

                if (load[i]!=null &&load[i].execute==true){
                    executeLoad(load[i]);
                }

            }
            for (int i = 0; i < store.length; i++) {
                if (store[i]!=null && store[i].execute==true && store[i].clkWB<clk ){
                    executeStore(store[i]);

                }

            }
            for (int i = 0; i < add.length; i++ ) {
                if (add[i]!=null &&add[i].execute==true && add[i].clkWB<clk){
                    executeAdd(add[i]);
                }
            }
            for (int i = 0; i < mul.length; i++) {
                if (mul[i]!=null && mul[i].execute==true && mul[i].clkWB<clk){
                    executeMul(mul[i]);

                }
            }
            if(stall==true)
                checkStall();
            CDBtaken=false;
            clk++;

            ;

            for(int i=0;i<32;i++) {
                System.out.print("F"+i+": "+reg[i]+" ");

            }
            System.out.println();
            System.out.print("Memory"+" ");
            for (int i = 0; i < Mem.length-1 ; i++) {
                System.out.print(Mem[i]+" ");
            }
            System.out.println();



        }while(!isEmpty());
    }

    public static void readFile(String fileName) throws IOException {
        File file1 = new File(fileName);
        BufferedReader br= new BufferedReader(new FileReader(file1));
        String st;
        while ((st = br.readLine()) != null)
        {
            instructions[n] = st;
            n++;
        }
        br.close();


    }


    public static void main(String [] args) throws IOException {
        readFile("mipsInst.txt");
        Mem[10]=10;
        Mem[20]=20;

        Scanner s = new Scanner(System.in);
        System.out.println("Enter Latency Load");
        latencyLoad= Integer.parseInt(s.nextLine());

        System.out.println("Enter Latency Store");
        latencyStore= Integer.parseInt(s.nextLine());

        System.out.println("Enter Latency Add");
        latencyAdd= Integer.parseInt(s.nextLine());

        System.out.println("Enter Latency Sub");
        latencySub= Integer.parseInt(s.nextLine());

        System.out.println("Enter Latency Mul");
        latencyMul= Integer.parseInt(s.nextLine());

        System.out.println("Enter Latency Div");
        latencyDiv= Integer.parseInt(s.nextLine());
        Tomasulo();


    }

}