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
        int availableProcessors = runtime.availableProcessors()-1;
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
            while(true)
            {
                long start = System.currentTimeMillis();
                miner.mine(blockChain);
                long finish = System.currentTimeMillis();
                long timeElapsed = finish - start;
                System.out.println("\n" + "BLOCKCHAIN:\n" + blockChain);
                System.out.println("Time: "+ timeElapsed +" s");
                System.out.println("Hashrate: " + miner.getTotalMines()/(timeElapsed/1000)+" H/s");
            }

        }catch (Exception ex)
        {
            System.out.print(ex.getMessage());
        }
        System.out.println("\n" + "BLOCKCHAIN:\n" + blockChain);
        System.out.println("Miner's reward: " + miner.getReward());
    }
}