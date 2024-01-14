package org.blockchain;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Miner {

    private double reward;

    private Block currentBlock;

    Range range;
    private long nonce;

    private long totalMines;
    private int availableProcessors;

    private Block incomingBlock;

    private ClientHandler clientHandler;


    public Miner(int availableProcessors) {
        this.availableProcessors = availableProcessors;
        this.clientHandler = new ClientHandler();
    }

    public void mine(BlockChain blockChain) throws Exception {

        List<Thread> threads=new ArrayList<>();
        this.clientHandler.setupMode=true;
        this.clientHandler.clear();
        this.currentBlock=clientHandler.getBlockFromServer();
        this.range = clientHandler.getRange();
        totalMines=0;
        this.clientHandler.setupMode=false;
        nonce=this.range.getStart();

        for (int i = 0; i < availableProcessors; i++) {
            Thread thread = new Thread(() -> {
                long localMines=0;
                while(!currentBlock.isGoldenHash()) {
                    MiningRange miningRange = getNonceRange();
                    if(miningRange==null){
                        passMines(localMines);
                        return;
                    }
                    for(long n=miningRange.getStart();n<miningRange.getEnd();n++)
                    {
                        currentBlock.generateHash(n);
                        localMines++;
                        if(currentBlock.isGoldenHash())
                        {
                            passMines(localMines);
                            return;
                        }
                        if(clientHandler.isRefreshBlock())
                        {
                            if(incomingBlock!=currentBlock) {
                                passMines(localMines);
                                return;
                            };
                        }
                    }
                }
                passMines(localMines);
                return;
            });
            threads.add(thread);
        }

        for (Thread thread:threads) {
            thread.start();
        }
        try {
            for (Thread thread : threads) {
                thread.join();
            }
        }catch (Exception ex)
        {
            throw new Exception(ex.getMessage());
        }
        if (currentBlock.isGoldenHash()) {
            System.out.println(this.currentBlock + " has just mined...");
            System.out.println("Hash is: " + this.currentBlock.getHash() + " Tries: " + nonce);
            //appending the block to the blockchain
            blockChain.addBlock(this.currentBlock);
            clientHandler.sendBlock(this.currentBlock);
            //calculating the reward
            reward += Constants.MINER_REWARD;
        }
    }

    // So miners will generate hash values until they find the right hash.
    //that matches with DIFFICULTY variable declared in class Constant
    public boolean notGoldenHash(Block block) {

        String leadingZeros = new String(new char[Constants.DIFFICULTY]).replace('\0', '0');

        return !block.getHash().substring (0, Constants.DIFFICULTY).equals (leadingZeros);
    }

    public synchronized void passMines(long minedNonces)
    {
        this.totalMines+=minedNonces;
    }
    public long getTotalMines()
    {
        return this.totalMines;
    }
    public synchronized MiningRange getNonceRange()
    {
        if(clientHandler.isRefreshBlock())
        {
            return null;
        }
        if(nonce+10000>this.range.getEnd())
        {
            try {
                this.range=clientHandler.getRange();
                if(this.range==null){
                    return null;
                }
                this.nonce=this.range.getStart();
            }catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        long temp = nonce;
        nonce+=10000;
        return new MiningRange(temp,nonce);
    }
    public double getReward() {
        return this.reward;
    }

    public Block getIncomingBlock() {
        return incomingBlock;
    }

    public void setIncomingBlock(Block incomingBlock) {
        this.incomingBlock = incomingBlock;
    }
}
