package org.main;
import  org.blockchain.Block;
import org.blockchain.BlockChain;
import org.blockchain.Constants;
import org.blockchain.Miner;


// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        Runtime runtime = Runtime.getRuntime();

        // Get the number of processors/threads available to the Java Virtual Machine
        int availableProcessors = runtime.availableProcessors();
        // First of all we instantiate the BlockChain class itself.
        BlockChain blockChain = new BlockChain();
        // we will instantiate the Miner class to fetch the minor object.
        Miner miner = new Miner(availableProcessors);

        //we have created the genesis or block 0
        // we will pass the id, the transaction string and previous hash
        //as this is the first block so we have to manually provide the previous hash

        //miner will take the transaction and will mine the block
        //to find the hash value and miner will append the block to Blockchain
        try {

            long start = System.currentTimeMillis();
            Block block0 = new Block(0, "transaction1", Constants.GENESIS_PREV_HASH);
            miner.mine(block0, blockChain);
            long finish = System.currentTimeMillis();
            long timeElapsed = finish - start;
            System.out.println("\n" + "BLOCKCHAIN:\n" + blockChain);
            System.out.println("Miner's reward: " + miner.getReward());
            System.out.println("Time: "+ timeElapsed +" s");
            System.out.println("Hashrate: " + miner.getTotalMines()/(timeElapsed/1000)+" H/s");


            //we will create the next block
            //we pass id, traction and this time the previous hash will contain
            // the hash value of Genesis block
            start = System.currentTimeMillis();
            Block block1 = new Block(1, "transaction2", blockChain.getBlockChain().get(blockChain.size() - 1).getHash());
            miner.mine(block1, blockChain);
            finish = System.currentTimeMillis();
            timeElapsed = finish - start;
            System.out.println("\n" + "BLOCKCHAIN:\n" + blockChain);
            System.out.println("Miner's reward: " + miner.getReward());
            System.out.println("Time: "+ timeElapsed +" s");
            System.out.println("Hashrate: " + miner.getTotalMines()/(timeElapsed/1000)+" H/s");

            start = System.currentTimeMillis();
            Block block2 = new Block(2, "transaction3", blockChain.getBlockChain().get(blockChain.size() - 1).getHash());
            miner.mine(block2, blockChain);
            finish = System.currentTimeMillis();
            timeElapsed = finish - start;
            System.out.println("\n" + "BLOCKCHAIN:\n" + blockChain);
            System.out.println("Miner's reward: " + miner.getReward());
            System.out.println("Time: "+ timeElapsed +" s");
            System.out.println("Hashrate: " + miner.getTotalMines()/(timeElapsed/1000)+" H/s");

        }catch (Exception ex)
        {
            System.out.print(ex.getMessage());
        }
        System.out.println("\n" + "BLOCKCHAIN:\n" + blockChain);
        System.out.println("Miner's reward: " + miner.getReward());
    }
}