package org.blockchain;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientHandler extends Thread{
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    private Block block;
    private boolean refreshBlock;

    public boolean setupMode;


    public ClientHandler(){
        try {
            setupMode=false;
            clientSocket = new Socket("127.0.0.1", 4000);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        refreshBlock=false;
    }

    public void run() {

    }
    public void clear() throws IOException{
        out.flush();
        while (in.ready()) {
            in.read();
        }
    }
    public Range getRange() throws IOException {
        int tries=0;
        while(tries<5) {
            try {
                if (out != null) {
                    out.println(":");
                } else if (clientSocket != null) {
                    out = new PrintWriter(clientSocket.getOutputStream(), true);
                    out.println(":");
                }
                Gson gson = new Gson();
                String inputLine = in.readLine();
                if(inputLine.equals("]")&&setupMode==false){
                    refreshBlock=true;
                    break;
                }
                Range range = gson.fromJson(inputLine, Range.class);
                if(range.getEnd()!=range.getStart()) {
                    return range;
                }else {
                    throw new Exception();
                }
            } catch (Exception e) {
                tries++;
                try {
                    Thread.sleep(100);
                }catch (Exception ex){

                }
            }
        }
        return null;
    }
    public Block getBlockFromServer() throws IOException {
        int tries=0;
        while(tries<5) {
            try {
        if(out!=null){
            out.println("[");
        }
        else if(clientSocket!=null){
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println("[");
        }
        Gson gson = new Gson();
        String inputLine = in.readLine();
        if(inputLine.equals("]")){
            inputLine = in.readLine();
        }
        Block b=gson.fromJson(inputLine,Block.class);
        refreshBlock=false;
        return b;
            } catch (Exception e) {
                tries++;
                try {
                    Thread.sleep(100);
                }catch (Exception ex){

                }
            }
        }
        return null;
    }

    public void sendBlock(Block sendBlock) throws IOException{
        if(out!=null){
            out.println("]");
            out.println(sendBlock.toJSON());
        }
        else if(clientSocket!=null){
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println("]");
            out.println(sendBlock.toJSON());
        }
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public boolean isRefreshBlock() {
        return refreshBlock;
    }

    public void setRefreshBlock(boolean refreshBlock) {
        this.refreshBlock = refreshBlock;
    }
}
