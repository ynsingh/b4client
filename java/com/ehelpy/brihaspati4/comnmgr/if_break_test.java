package com.ehelpy.brihaspati4.comnmgr;

import java.util.Random;

public class if_break_test {

	public static void main(String[] args) {
		
		int i;
		
		while(true)
		{
			i =increment();
		
			if(i<=25)
			{
				System.out.println("i : "+i);
				break;
			}
			else
				System.out.println("i : "+i);
		}
	}

	private static int increment()
	{
		Random rand = new Random(); 
		int value = rand.nextInt(50);
		return value; 
		
	}

}
