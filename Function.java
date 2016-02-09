package PIRM15S2;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Function {

	
	static ArrayList<Food> FoodList = new ArrayList<Food>();
	static ArrayList<Food> OkFoodList = new ArrayList<Food>();
	static ArrayList<Food> ExFoodList = new ArrayList<Food>();
	static ArrayList<Food> TempFoodList = new ArrayList<Food>();
	
	static ArrayList<Consumer> ConsumerList =new ArrayList<Consumer>();
		
	static ArrayList<String> foodlist = new ArrayList<String>();
	static ArrayList<String> userlist = new ArrayList<String>();
	static ArrayList<String> insgroup = new ArrayList<String>();
	static ArrayList<String> instruction = new ArrayList<String>();
	static ArrayList<String> historyinsgroup = new ArrayList<String>();
	static ArrayList<String> tempexeinsgroup = new ArrayList<String>();
	static String todaydate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

    
    static double consumecost;
    
  
    static  double []eachfoodokquantity ;
    static  double []eachfoodexquantity ;
    
    
    
    static String manipdate;
    static String logpath;
    
    public static void eachfoodquantclear(double[] a){
    	for(int i =0; i<foodlist.size(); i++){
    		a[i] = 0.0;
    	}
    }
   


	

	
	
	/* InstructionInput can receive instruction input and find corresponding method.
	 * 
	 */
	public static void InstructionInput(String foodpath, String userpath, String instructionpath, String logspath) throws IOException{
		
		instruction.clear();
		FoodList.clear();
		historyinsgroup.clear();
		/*
		String foodpath = foodp;
		String userpath = userp;
		String inspath = insp;
		String logpath = logp;
		*/
		logpath = logspath;
		FileReader fileusers = new FileReader(userpath);
		Scanner users =new Scanner(fileusers);
		while(users.hasNextLine())
		{
			userlist.add(users.nextLine());
			
		}
		users.close();
		FileReader filefoods =new FileReader(foodpath);
		Scanner foods =new Scanner(filefoods);
		while(foods.hasNextLine())
		{
			foodlist.add(foods.nextLine());
			
		}
		foods.close();
		
		FileReader fileins = new FileReader(instructionpath);
		Scanner in = new Scanner(fileins);
		
		
		
		//logpath = logp;
		
		 
		while(in.hasNextLine())
		{
			insgroup.add(in.nextLine());
			
		}
		in.close();
		int insnum = insgroup.size();
		
		
		for(int i = 0; i< insnum; i++){
			instruction.clear();
			
			// decompose the ith instruction
			String[] temp = insgroup.get(i).split(" ");
		    int n = temp.length;
		    
		    for(int j = 0; j<n; j++){
		    	instruction.add(temp[j]);
		    }
			String first = instruction.get(0);
		    switch(first){
			case "BUY": { 
				Buy();
			
			}; break;
			
			case "EAT":{
				Eat();
			}; break;
			
			case "DISCARD":{
				Discard();
			}; break;
			
			case "QUERY": Query(); break;
			
			default: ;break;
		
		
		    }
			
		}
		
	
		
	}

/*
 * *******************************************
 * 
 * 
 * 		manipulation method
 * 
 * 
 * *******************************************	
 */
	
	
	public static void Buy() throws IOException{
		
		if(instruction.size() != 6) return;
		
		manipdate = instruction.get(1);
		String name = instruction.get(2);
		String ubdate = "";
		if(instruction.get(3).contains("-")){
			if(instruction.get(3).split("-").length == 3)
				ubdate = instruction.get(3);
			else return;
		}
		else return;
		
		double count = Double.parseDouble(instruction.get(4));
		String cost = "";
		if(instruction.get(5).charAt(0) == '$'){
			if(isNumeric(instruction.get(5).substring(1))){
				if(Double.parseDouble(instruction.get(5).substring(1)) > 0){
					cost = instruction.get(5);
				}
				else return;
				
				 
			}
			else return;
		}
		else return;
		//String cost = instruction.get(5);
		double allcost = Double.parseDouble(cost.substring(1));
		
		// check food type exists
		if(!FoodTypeCheck(name)) return;
		if(! (DateFormateCheck(instruction.get(1)) && DateFormateCheck(ubdate) )  ) return;
		
		FoodList.add(new Food(name, ubdate, count, allcost));
		AscendingSort(FoodList);
		
		FoodDistinguish(manipdate);
		PrintInstruction();
		SuggestiontoBuy();
		SuggestiontoDiscard();
		
		String ins = "";
		for (String c: instruction){
			ins = ins + c + " ";
		}
		
		historyinsgroup.add(ins);
		
	}
	
	
	
	
	public static void Eat() throws IOException{
		String consumetime = instruction.get(1);
		manipdate = instruction.get(1);
		String foodtype = instruction.get(2);
		double amount = Double.parseDouble(instruction.get(3));
		String consumer = instruction.get(4);
		int buffer  = 0;
		double tempquanty;
		consumecost = 0;
		double[] eachfoodokquantity= new double[foodlist.size()];
		
		FoodDistinguish(consumetime);
		int m = OkFoodList.size();
		//eachfoodokquantclear();
		
		
		for(int i = 0; i<foodlist.size(); i++){
			for(int j = 0 ; j<m; j++){
				if(foodlist.get(i).equalsIgnoreCase(OkFoodList.get(j).type))
					eachfoodokquantity[i]+=OkFoodList.get(j).quantity;
			}
		}
		
		for(int i = 0; i<foodlist.size(); i++){
			if (foodtype.equalsIgnoreCase(foodlist.get(i)))
				buffer = i;
		}
		
		if(eachfoodokquantity[buffer] < amount ) return;
		else{
			
			tempquanty = amount;
			AscendingSort(OkFoodList);
			int n = OkFoodList.size();
			for(int i = 0; i<n; i++){
				if(OkFoodList.get(i).type.equalsIgnoreCase(foodtype)){
					if(OkFoodList.get(i).quantity < amount){
						amount-= OkFoodList.get(i).quantity;
						//OkFoodList.get(i).quantity =0;
						consumecost += OkFoodList.get(i).quantity *OkFoodList.get(i).unitprice;
						OkFoodList.get(i).quantity =0;
					}
					else{
						OkFoodList.get(i).quantity -= amount;
						consumecost =consumecost+ amount*OkFoodList.get(i).unitprice;
						amount =0;
						break;
					}
				}
				
			}
			ConsumerList.add(new Consumer(foodtype, consumer, consumetime,tempquanty,consumecost));
		    for(int a = 0; a < OkFoodList.size(); a++){
		    	if(OkFoodList.get(a).quantity == 0){
		    		OkFoodList.remove(a);
		    	}
		    }
			FoodList.clear();
			for(int b = 0; b < ExFoodList.size(); b++){
		    	FoodList.add(ExFoodList.get(b));
		    }
		    for(int c = 0; c < OkFoodList.size(); c++){
		    	FoodList.add(OkFoodList.get(c));
		    }
		    PrintInstruction();
			SuggestiontoBuy();
			SuggestiontoDiscard();
			
			String ins = "";
			for (String c: instruction){
				ins = ins + c + " ";
			}
			
			historyinsgroup.add(ins);
			
		}
				
			
			
		}

	
	public static void Discard() throws IOException{
		String distime = instruction.get(1);
		manipdate = instruction.get(1);
		String foodtype = instruction.get(2);
		double amount = Double.parseDouble(instruction.get(3));
		int buffer = 0;
		double [] eachfoodexquantity = new double[foodlist.size()];
		
		FoodDistinguish(distime);
		int m = ExFoodList.size();
		
		
		
		for(int i = 0; i<foodlist.size(); i++){
			for(int j = 0 ; j<m; j++){
				if(foodlist.get(i).equalsIgnoreCase(ExFoodList.get(j).type))
					eachfoodexquantity[i] += ExFoodList.get(j).quantity ;
			}
		}
		
		for(int i = 0; i<foodlist.size(); i++){
			if (foodtype.equalsIgnoreCase(foodlist.get(i)))
				buffer = i;
		}
		
		if(eachfoodexquantity[buffer] < amount ) return;
		else{
			
			AscendingSort(ExFoodList);
			int n = ExFoodList.size();
			for(int i = 0; i<n; i++){
				
				if(ExFoodList.get(i).type.equalsIgnoreCase(foodtype)){
					
					if(ExFoodList.get(i).quantity < amount){
						amount-= ExFoodList.get(i).quantity;
						ExFoodList.get(i).quantity =0;
		
					}
					else{
						ExFoodList.get(i).quantity -= amount;
						amount =0;
						break;
					}
				}
				
			}
			
		    for(int a = 0; a < ExFoodList.size(); a++){
		    	if(ExFoodList.get(a).quantity == 0){
		    		ExFoodList.remove(a);
		    	}
		    }
			FoodList.clear();
			for(int b = 0; b < ExFoodList.size(); b++){
		    	FoodList.add(ExFoodList.get(b));
		    }
		    for(int c = 0; c < OkFoodList.size(); c++){
		    	FoodList.add(OkFoodList.get(c));
		    }
		    
		    PrintInstruction();
			SuggestiontoBuy();
			SuggestiontoDiscard();
			
			
			
		}
	
	}
		
		
		
	
	
/*
 * *******************************************
 * 
 * 
 * 		query method
 * 
 * 
 * *******************************************
 */
	
	
	/*Query is used to respond to all queries.
	 * 
	 */
	public static void Query() throws IOException{
		int l = instruction.size();
		String queryTime = instruction.get(1);
		int size = historyinsgroup.size();
		String[] lasthisIns = historyinsgroup.get(size-1).split(" ");
		String lasthisTime = lasthisIns[1];
		String[] firsthisIns = historyinsgroup.get(0).split(" ");
		String firsthisTime = firsthisIns[1];
		if(!DateCompare(queryTime, lasthisTime))
		{
			switch(l){
			case 2: ShowAllFood(); break;
			case 3: ShowSpecificFood(instruction.get(2), instruction.get(1));break;
			case 4: ConsumeDetail(instruction.get(1), instruction.get(2), instruction.get(3));break;
			default:;break;
			}
		}
		
		else{ 
			
			if(!DateCompare(queryTime, firsthisTime)){
				//l ==4 find in Consumer
				if(l == 4){
					ConsumeDetail(instruction.get(1), instruction.get(2), instruction.get(3));
				}
				// otherwise execute ins first, just query all rest food or specific food
				else{
					tempexeinsgroup.clear();
					//first setup tempexeinsgroup
					for(int i = 0; i < size; i++ ){
						String [] temp = historyinsgroup.get(i).split(" ");
						String hisinstime = temp[1];
						
						if(!DateCompare(queryTime,hisinstime)){
							tempexeinsgroup.add(historyinsgroup.get(i));
						}
					}
					//clear TemFoodList
					TempFoodList.clear();
					//execute tempexeinsgroup in TempFoodList
					for(int i = 0; i< tempexeinsgroup.size(); i++){
						String [] singletempins = tempexeinsgroup.get(i).split(" ");
						String first = singletempins[0];
						
						
						  switch(first){
							case "BUY": { 
								// begin execute BUY in TempFoodList
							
								String buytime = singletempins[1];
								String name = singletempins[2];
								String ubdate = singletempins[3];
								double count = Double.parseDouble(singletempins[4]);
								String cost = singletempins[5];
								double allcost = Double.parseDouble(cost.substring(1));
								if(DateCompare(buytime, ubdate)){
								TempFoodList.add(new Food(name, ubdate, count, allcost));
								}
								
								
								
							}; break;
							
							case "EAT":{
								// begin execute EAT in TempFoodList
								String consumetime = singletempins[1];
								String foodtype = singletempins[2];
								double amount = Double.parseDouble(singletempins[3]);
								while(amount != 0){
									//later
									double am = 0;
									FoodDistinguishInTempFood(consumetime);
									AscendingSort(OkFoodList);
									for(Food g: OkFoodList){
										if(g.type.equalsIgnoreCase(foodtype))
										{ am = am + g.quantity;}
									}
									if( am > amount || am == amount) {
										for(Food f: TempFoodList){
											if(f.type.equalsIgnoreCase(foodtype))
											{
												if(!DateCompare(f.offdate, consumetime)){
													if(f.quantity > amount) {
														f.quantity = f.quantity - amount;
														amount = 0;
													}
													else {
														if(f.quantity >0){
															amount = amount - f.quantity;
															f.quantity = 0;
														}
													}
												}
											}
										}
									}
									
									
									/*
									for(Food f: TempFoodList){
										if(f.type.equalsIgnoreCase(foodtype))
										{
											if(!DateCompare(f.offdate, consumetime)){
												if(f.quantity > amount) {
													f.quantity = f.quantity - amount;
													amount = 0;
												}
												else {
													if(f.quantity >0){
														amount = amount - f.quantity;
														f.quantity = 0;
													}
												}
											}
										}
									}
									*/
								}							
							}; break;
						default: ;break;
						    }
					
					}
					
					
					// at this time tempfoodlist arrive the status at the query time
					if(l ==2){
						OkFoodList.clear();
						ExFoodList.clear();
						for(int i = 0; i< TempFoodList.size(); i++){
							if(!DateCompare(TempFoodList.get(i).offdate, queryTime)){
								OkFoodList.add(TempFoodList.get(i));
							}
							
							// later
							else{
								ExFoodList.add(TempFoodList.get(i));
							}
							
						}
						
						//int f = foodlist.size();
						//double[] okfoodquantity = new double [f];
						int n = OkFoodList.size();
						
						AscendingSort(OkFoodList);
						PrintInstruction();
						System.out.println("PIRM:");
						PrintlnLog("PIRM:");
						for(int j = 0; j<n ; j++){
							if(OkFoodList.get(j).quantity!=0){
							System.out.println(OkFoodList.get(j).quantity+" units of "+OkFoodList.get(j).type+" that should be eaten before "+OkFoodList.get(j).offdate);
							PrintlnLog(OkFoodList.get(j).quantity+" units of "+OkFoodList.get(j).type+" that should be eaten before "+OkFoodList.get(j).offdate);
							}
						}
						
						// begin to discard
						ArrayList<String> discardfoodname = new ArrayList<String>();
						
						
						int nn = foodlist.size();
						double[] exfoodquantity = new double [foodlist.size()];
						int m = ExFoodList.size();
						for(int i = 0; i<nn; i++){
							for(int j = 0 ; j<m; j++){
								if(foodlist.get(i).equalsIgnoreCase(ExFoodList.get(j).type))
									exfoodquantity[i] += ExFoodList.get(j).quantity ;
							}
						}
						double[] flag = new double[nn];
						int f= 0;
						for(int index= 0; index< nn; index++){
							if(exfoodquantity[index]!=0){
								discardfoodname.add(foodlist.get(index));
								// later
								flag[f] = exfoodquantity[index];
								f++;
							}
						}
						
						if(discardfoodname.size()!=0){
							System.out.print("Suggest to discard ");
							PrintLog("Suggest to discard ");
							for (int c = 0; c<discardfoodname.size();c++){
								if(c==discardfoodname.size()-1){
									System.out.println(flag[c]+" units of "+discardfoodname.get(c)+"\n");
									PrintlnLog(flag[c]+" units of "+discardfoodname.get(c)+"\n");
								}
								else{
									System.out.print(flag[c]+" units of "+discardfoodname.get(c)+", ");
									 PrintLog(flag[c]+" units of "+discardfoodname.get(c)+", ");
								}
								
							}
						}
						else{
							System.out.println("No need to discard! "+'\n');
							PrintlnLog("No need to discard! "+'\n');
						}
						
						
						
						
						
						
						
						//end of discard
						System.out.print("\n");
						PrintLog("\n");
						
					}
					else{
						if(l == 3){
							OkFoodList.clear();
							ExFoodList.clear();
							for(int i = 0; i< TempFoodList.size(); i++){
								if(!DateCompare(TempFoodList.get(i).offdate, queryTime)){
									OkFoodList.add(TempFoodList.get(i));
								}
								
							}
							ArrayList<Food> temp = new ArrayList<Food>();
							int n = OkFoodList.size();
							double amount = 0;
							double cost = 0;
							for (int i = 0 ; i < n; i++ ){
								if(instruction.get(2).equalsIgnoreCase(OkFoodList.get(i).type))
									temp.add(OkFoodList.get(i));
							}
							AscendingSort(temp);
							
							int m = temp.size();
							String earliest = temp.get(0).offdate;
							
							for(int i = 0 ; i < m; i++){
								amount += temp.get(i).quantity;
								cost += temp.get(i).quantity * temp.get(i).unitprice;
							}
							PrintInstruction();
							System.out.println("PIRM:"+'\n'+"The fridge contains: "+"\n"+amount+" units of "+instruction.get(2) );
							PrintlnLog("PIRM:"+'\n'+"The fridge contains: "+"\n"+amount+" units of "+instruction.get(2) );
							//System.out.println("The earliest use-by date for the "+food +" is " +earliest);
							//PrintlnLog("The earliest use-by date for the "+food +" is " +earliest);
							System.out.println("The total cost of the remaining "+instruction.get(2) +" is $"+ cost+"\n");
							PrintlnLog("The total cost of the remaining "+instruction.get(2) +" is $"+ cost+"\n");
						}
					}
					
					
				}
			}
		}
	}
	

	
	
	
	
	
	
	
	
	
/*
 ******************************************
	
		supportive method
	
	
******************************************
*/
	
	

	
	
	/* DateCompare judge two dates which is late
	 * if return true, d2 is later
	 * if return false, d1 is later
	 */
	public static boolean DateCompare(String d1, String d2){
		boolean formerispast = true;
		
		String[] d1s = d1.split("-");
		String[] d2s = d2.split("-");
		int d1year = Integer.parseInt(d1s[2]);
		int d1month = Integer.parseInt(d1s[1]);
		int d1day = Integer.parseInt(d1s[0]);

		int d2year = Integer.parseInt(d2s[2]);
		int d2month = Integer.parseInt(d2s[1]);
		int d2day = Integer.parseInt(d2s[0]);
		
		if( d1year >d2year) formerispast = false;
		if(d1year == d2year) {
			if(d1month > d2month) formerispast = false;
			else{
				if(d1month == d2month){
					if(d1day >d2day ||d1day ==d2day) formerispast =false;
				}
			}
			
		}
		
		return formerispast ;
	}
	
	
	public static String Findearliestdate(ArrayList<String> a){
		String earliest = a.get(0);
		for(int i = 1; i<a.size(); i++){
			if(DateCompare(a.get(i),earliest)) earliest = a.get(i);
		}
		return earliest;
	}
	
	
	/* AscendingSort can settle the ArrayList in ascending order according to date,
	 * this means the bigger the index is , the later the date is.
	 */
	public static void AscendingSort(ArrayList <Food> sample){
		if(sample.size() ==0) { return;
			//System.out.println("Fridge is empty");
			//SuggestiontoBuy();
		}
		else{
			// already add size ==1 condition
			if(sample.size() ==1){ return;
				
			}
			else{
			for(int i = sample.size(); i>1; i--){
				for(int j = 0; j<i-1; j++){
					if(! DateCompare(sample.get(j).offdate,sample.get(j+1).offdate)){
						Food temp = new Food(sample.get(j+1));
						Food.copy(sample.get(j+1), sample.get(j));
						Food.copy(sample.get(j), temp);
					}
				}
			}
			}
		}
		
	}
	
	public static void FoodDistinguish(String mdate){
		OkFoodList.clear();
		ExFoodList.clear();
		for(int i = 0; i< FoodList.size(); i++){
			if(!ExpireCheck(FoodList.get(i).offdate, mdate)){
				OkFoodList.add(FoodList.get(i));
			}
			else{
				ExFoodList.add(FoodList.get(i));
			}
		}
	}
	public static void FoodDistinguishInTempFood(String mdate){
		OkFoodList.clear();
		ExFoodList.clear();
		for(int i = 0; i< TempFoodList.size(); i++){
			if(!ExpireCheck(TempFoodList.get(i).offdate, mdate)){
				OkFoodList.add(TempFoodList.get(i));
			}
			else{
				ExFoodList.add(TempFoodList.get(i));
			}
		}
	}
	public static void FoodDistinguishWithDate(String date){
		OkFoodList.clear();
		ExFoodList.clear();
		for(int i = 0; i< FoodList.size(); i++){
			if(!DateCompare(FoodList.get(i).offdate, date)){
				OkFoodList.add(FoodList.get(i));
			}
			else{
				ExFoodList.add(FoodList.get(i));
			}
		}
	}
	
	
	/* SuggestiontoBuy execute if any food is 0, suggest to buy that food. 
	 * 
	 */
	public static void SuggestiontoBuy() throws IOException{
		ArrayList<String> lackfoodname = new ArrayList<String>();
		FoodDistinguish(manipdate);
		double []eachfoodokquantity = new double[foodlist.size()];
		int m = OkFoodList.size();
		
		for(int i = 0; i<foodlist.size(); i++){
			for(int j = 0 ; j<m; j++){
				if(foodlist.get(i).equalsIgnoreCase(OkFoodList.get(j).type))
					{
					eachfoodokquantity[i]+=1.0;
					}
			}
		}
		
		for(int index= 0; index< foodlist.size(); index++){
			if(eachfoodokquantity[index]==0){
				lackfoodname.add(foodlist.get(index));
			}
		}
		
		if(lackfoodname.size()!=0){
			System.out.print("Suggest to buy ");
			PrintLog("PIRM:"+'\n'+"Suggest to buy ");
			for (int c = 0; c<lackfoodname.size();c++){
				if(c==lackfoodname.size()-1){
					System.out.print(lackfoodname.get(c));
					PrintLog(lackfoodname.get(c));
				}
				else{
					System.out.print(lackfoodname.get(c)+", ");
					 PrintLog(lackfoodname.get(c)+", ");
				}
				
			}
			System.out.print('\n');
			PrintLog("\n");
		}
		
	}
	
	
	
	public static void SuggestiontoDiscard() throws IOException{
		
		ArrayList<String> discardfoodname = new ArrayList<String>();
		
		FoodDistinguish(manipdate);
		
		int n = foodlist.size();
		double[] exfoodquantity = new double [foodlist.size()];
		int m = ExFoodList.size();
		for(int i = 0; i<n; i++){
			for(int j = 0 ; j<m; j++){
				if(foodlist.get(i).equalsIgnoreCase(ExFoodList.get(j).type))
					exfoodquantity[i] += ExFoodList.get(j).quantity ;
			}
		}
		double [] flag = new double[n];
		int f = 0;
		for(int index= 0; index< n; index++){
			if(exfoodquantity[index]!=0){
				discardfoodname.add(foodlist.get(index));
				//later
				flag[f] = exfoodquantity[index];
				f++;
			}
		}
		
		if(discardfoodname.size()!=0){
			System.out.print("Suggest to discard ");
			PrintLog("Suggest to discard ");
			for (int c = 0; c<discardfoodname.size();c++){
				if(c==discardfoodname.size()-1){
					System.out.println(flag[c]+" units of "+discardfoodname.get(c)+"\n");
					PrintlnLog(flag[c]+" units of "+discardfoodname.get(c)+"\n");
				}
				else{
					System.out.print(flag[c]+" units of "+discardfoodname.get(c)+", ");
					 PrintLog(flag[c]+" units of "+discardfoodname.get(c)+", ");
				}
				
			}
		}
		else{
			System.out.println("No need to discard! "+'\n');
			PrintlnLog("No need to discard! "+'\n');
		}
			
	}
	
public static void SuggestiontoDiscardInTempFood(String Etime) throws IOException{
		
		ArrayList<String> discardfoodname = new ArrayList<String>();
		
		FoodDistinguishInTempFood(Etime);
		
		int nn = foodlist.size();
		double[] exfoodquantity = new double [foodlist.size()];
		int m = ExFoodList.size();
		for(int i = 0; i<nn; i++){
			for(int j = 0 ; j<m; j++){
				if(foodlist.get(i).equalsIgnoreCase(ExFoodList.get(j).type))
					exfoodquantity[i]++ ;
			}
		}
		
		for(int index= 0; index< nn; index++){
			if(exfoodquantity[index]!=0){
				discardfoodname.add(foodlist.get(index));
			}
		}
		
		if(discardfoodname.size()!=0){
			System.out.print("Suggest to discard ");
			PrintLog("Suggest to discard ");
			for (int c = 0; c<discardfoodname.size();c++){
				if(c==discardfoodname.size()-1){
					System.out.println(discardfoodname.get(c)+"\n");
					PrintlnLog(discardfoodname.get(c)+"\n");
				}
				else{
					System.out.print(discardfoodname.get(c)+", ");
					 PrintLog(discardfoodname.get(c)+", ");
				}
				
			}
		}
		else{
			System.out.println("No need to discard! "+'\n');
			PrintlnLog("No need to discard! "+'\n');
		}
			
	}
	
	/* ShowAllFood is used to show all food.
	 * It is respond to "Query 17-04-2015" format.
	 */
	public static void ShowAllFood() throws IOException{
		//String temp;
		String inputdate = instruction.get(1);
		
		FoodDistinguishWithDate(inputdate);
		
		//int f = foodlist.size();
		//double[] okfoodquantity = new double [f];
		int n = OkFoodList.size();
		
		if(n!=0)
		{	
		AscendingSort(OkFoodList);
		PrintInstruction();
		System.out.println("PIRM:");
		PrintlnLog("PIRM:");
		for(int i = 0; i<n ; i++){
			if(OkFoodList.get(i).quantity !=0){
			System.out.println(OkFoodList.get(i).quantity+" units of "+OkFoodList.get(i).type+" that should be eaten before "+OkFoodList.get(i).offdate);
			PrintlnLog(OkFoodList.get(i).quantity+" units of "+OkFoodList.get(i).type+" that should be eaten before "+OkFoodList.get(i).offdate);
			}
		}
		//later
		ArrayList<String> discardfoodname = new ArrayList<String>();
		int nn = foodlist.size();
		double[] exfoodquantity = new double [foodlist.size()];
		int m = ExFoodList.size();
		for(int i = 0; i<nn; i++){
			for(int j = 0 ; j<m; j++){
				if(foodlist.get(i).equalsIgnoreCase(ExFoodList.get(j).type))
					exfoodquantity[i] += ExFoodList.get(j).quantity ;
			}
		}
		double [] flag = new double[nn];
		int f = 0;
		for(int index= 0; index< nn; index++){
			if(exfoodquantity[index]!=0){
				discardfoodname.add(foodlist.get(index));
				//later
				flag[f] = exfoodquantity[index];
				f++;
			}
		}
		
		if(discardfoodname.size()!=0){
			System.out.print("Suggest to discard ");
			PrintLog("Suggest to discard ");
			for (int c = 0; c<discardfoodname.size();c++){
				if(c==discardfoodname.size()-1){
					System.out.println(flag[c]+" units of "+discardfoodname.get(c)+"\n");
					PrintlnLog(flag[c]+" units of "+discardfoodname.get(c)+"\n");
				}
				else{
					System.out.print(flag[c]+" units of "+discardfoodname.get(c)+", ");
					 PrintLog(flag[c]+" units of "+discardfoodname.get(c)+", ");
				}
				
			}
		}
		else{
			System.out.println("No need to discard! "+'\n');
			PrintlnLog("No need to discard! "+'\n');
		}
		
		
		
		//
		System.out.print("\n");
		PrintLog("\n");
		}
	}
	
	
	//Big Problem
	/* ShowSpecificFood is used to show the specific food.
	 * It is respond to "Query 17-04-2015 milk" format.
	 */
	public static void ShowSpecificFood(String food, String inputdate) throws IOException{
		FoodDistinguishWithDate(inputdate);
		ArrayList<Food> temp = new ArrayList<Food>();
		int n = OkFoodList.size();
		double amount = 0;
		double cost = 0;
		for (int i = 0 ; i < n; i++ ){
			if(food.equalsIgnoreCase(OkFoodList.get(i).type))
				temp.add(OkFoodList.get(i));
		}
		AscendingSort(temp);
		
		int m = temp.size();
		
		if(m!=0){
		String earliest = temp.get(0).offdate;}
		
		for(int i = 0 ; i < m; i++){
			amount += temp.get(i).quantity;
			cost += temp.get(i).quantity * temp.get(i).unitprice;
		}
		PrintInstruction();
		System.out.println("PIRM:"+'\n'+"The fridge contains: "+"\n"+amount+" units of "+food);
		PrintlnLog("PIRM:"+'\n'+"The fridge contains: "+"\n"+amount+" units of "+food);
		//System.out.println("The earliest use-by date for the "+food +" is " +earliest);
		//PrintlnLog("The earliest use-by date for the "+food +" is " +earliest);
		System.out.println("The total cost of the remaining "+food +" is $"+ cost+"\n");
		PrintlnLog("The total cost of the remaining "+food +" is $"+ cost+"\n");
	
	
		
	}
	
	
	
	public static void ConsumeDetail(String date, String food, String consumer) throws IOException{
		Consumer.AscendingSort(ConsumerList);
		ArrayList<Consumer> temp = new ArrayList<Consumer>();
		double tempcost = 0;
		double tempcount =0;
		for(Consumer c:ConsumerList){
			if(!DateCompare(date, c.date)) temp.add(c);
		}
		for(Consumer d:temp){
			if((food.equalsIgnoreCase(d.foodtype) && consumer.equalsIgnoreCase(d.consumername))) 
				{
				tempcost += d.eatcost;
				tempcount +=d.quantity;
				}
		}
		PrintInstruction();
		System.out.println(consumer + " has consumed " +tempcount+" unit of "+food+" at a total cost of "+"$"+tempcost+"\n");
		
		PrintlnLog("PIRM:"+'\n'+consumer + " has consumed " +tempcount+" unit of "+food+" at a total cost of "+"$"+tempcost+"\n");		
		
	}
	


	public static  boolean easygreater(String d1, String d2){
		boolean latterisgreater =true;
		
		String[] d1s = d1.split("-");
		String[] d2s = d2.split("-");
		int d1year = Integer.parseInt(d1s[2]);
		int d1month = Integer.parseInt(d1s[1]);
		int d1day = Integer.parseInt(d1s[0]);

		int d2year = Integer.parseInt(d2s[2]);
		int d2month = Integer.parseInt(d2s[1]);
		int d2day = Integer.parseInt(d2s[0]);
		
		if( d1year >d2year) latterisgreater = false;
		if(d1year == d2year) {
			if(d1month > d2month) latterisgreater = false;
			else{
				if(d1month == d2month){
					if(d1day >d2day ) latterisgreater =false;
				}
			}
			
		}
		
		return latterisgreater;
	}
	
	
	
	/* ExpiryCheck check if date is off date compared to today
	 * return true means  expire
	 * return false means not expire
	 */
	static boolean ExpireCheck (String fooddate, String buydate ){
		boolean expire = false;
		expire =DateCompare(fooddate, buydate);
		return expire;	
	}
	

	
	public static void PrintInstruction() throws IOException{
		System.out.print("User: ");
		PrintLog("User: ");
		for (int i = 0; i< instruction.size(); i++){
			System.out.print(instruction.get(i)+ " ");
			PrintLog(instruction.get(i)+ " ");
		}
		System.out.print("\n");
		PrintLog("\n");
	}
	
	
	
	/* write log file
	 * 
	 */
	public static void PrintlnLog(String s) throws IOException{
		PrintWriter out  = new PrintWriter(new FileWriter(logpath,true));
		
		out.println(s);
		out.close();
		
	}
	
	public static void PrintLog(String s) throws IOException{
		PrintWriter out  = new PrintWriter(new FileWriter(logpath,true));
		
		out.print(s);
		out.close();
		
	}

	public static boolean FoodTypeCheck(String food){
		boolean existence = false;
		for(String c:foodlist){
			if(food.equalsIgnoreCase(c))
				existence = true;
		}
		return existence;
		
	}
	
	public static boolean UserNameCheck(String name){
		boolean existence = false;
		for(String c:userlist){
			if(name.equalsIgnoreCase(c))
				existence = true;
		}
		return existence;
	}
	
	public static boolean DateFormateCheck(String date){
		boolean dateformat =true;
		String[] temp = date.split("-");
		
		if(temp.length != 3 ) dateformat =false;
		
		String day = temp[0];
		int d = Integer.parseInt(day);
		if( d<1 || d>31) dateformat = false; 
		
		String month = temp[1];
		int m = Integer.parseInt(month);
		if( m<1 || m>12) dateformat = false;
		
		String year = temp[2];
		int y = Integer.parseInt(year);
		if( y<1 || y>9999) dateformat = false; 
		
		
		
		return dateformat;
	}
		
	public static boolean isNumeric(String str)  
	{  
	  try  
	  {  
	    double d = Double.parseDouble(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
}





