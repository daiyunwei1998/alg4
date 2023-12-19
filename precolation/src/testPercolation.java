
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.*;


public class testPercolation {
    public Percolation readFile(String path) {

        Percolation p = null;
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            // Read the first line containing a single number
            String firstLine = br.readLine();
            int N = Integer.parseInt(firstLine.trim());
            p = new Percolation(N);

            String line;
            while ((line = br.readLine()) != null) {
                String[] coordinates = line.trim().split("\\s+");
                if (coordinates.length == 2) {
                    int row = Integer.parseInt(coordinates[0]);
                    int col = Integer.parseInt(coordinates[1]);
                    //System.out.println("(" + row + ", " + col + ")");
                    p.open(row, col);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p;


    }


    @Test
    public void test1(){
        Percolation p = new Percolation(10);
        p.open(1,2);
        p.open(2,2);
        p.open(3, 2);
        p.open(4,2);
        p.open(5,2);
        p.open(6,2);
        p.open(7,2);
        p.open(8,2);
        p.open(9,2);
        p.open(10,2);
       // boolean ans =  p.isConnectedToBottom(1,2);
        boolean ans1 =  p.percolates();
        System.out.print(ans1);
        System.out.println();
        //p.printInternal();
    }

    @Test
    public void test2(){
        Percolation p = new Percolation(10);
        p.open(10,3);


       // boolean ans =  p.isConnectedToBottom(10,3);
        System.out.print(1);
        System.out.println();
        //p.printInternal();
    }

    @Test
    public void test3(){
        Percolation p = new Percolation(10);
        p.open(10,3);


        //boolean ans =  p.isConnectedToBottom(10,3);
        System.out.print(1);
        System.out.println();
        //p.printInternal();
    }

    @Test
    public void input6(){
        Percolation p = readFile("C:\\Users\\daiyu\\Desktop\\alg4\\precolation\\testResource\\input6.txt");

       // boolean ans =  p.isConnected(5,1,5,2);
        System.out.print(1);
        System.out.println();
        p.percolates();
    }

    @Test
    public void input20(){
        //backwash case
        Percolation p = readFile("C:\\Users\\daiyu\\Desktop\\alg4\\precolation\\testResource\\input20.txt");

        boolean ans =  p.percolates();
        System.out.print(ans);
        System.out.println();
        p.printInternal();
    }

}
