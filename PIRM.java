package PIRM15S2;

import java.io.IOException;
import java.util.Scanner;

public class PIRM {
	static boolean keep = true;
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		/*
		Scanner inpath = new Scanner(System.in);
		String foodpath = inpath.next();
		String userpath = inpath.next();
		String inspath = inpath.next();
		String logspath = inpath.next();
		inpath.close();
		*/
		String foodpath = args[0];
		String userpath = args[1];
		String instructionpath = args[2];
		String logspath = args[3];
		
		Function.InstructionInput(foodpath, userpath, instructionpath, logspath);
		/*
		for(String s :Function.historyinsgroup){
			System.out.println(s);
		}
		*/
	}
}
