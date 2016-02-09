package PIRM15S2;

import java.util.ArrayList;

public class Consumer {
	public String date;
	public String foodtype;
	public String consumername;
	public double quantity;
	public double eatcost;
	
	
	public Consumer(String food , String name, String time, double count, double totalcost){
		
		foodtype = food;
		consumername = name;
		date = time;
		quantity = count;
		eatcost = totalcost;
		
		
	}
	
	
	public static void Count (ArrayList<Consumer> a){
		
		int n = a.size();
		
		System.out.println("elements of this arryalist is : "+n);
		
	}
	
	public void ShowValues(){
		
		System.out.println("$"+eatcost+" "+foodtype+" consumed by "+ consumername +" in "+date );
	}
	
	
	public Consumer(Consumer a){
		this.foodtype = a.foodtype;
		this.consumername = a.consumername;
		this.date = a.date;
		this.quantity = a.quantity;
		this.eatcost =a.eatcost;
	}
	
	public static void copy(Consumer a, Consumer b){
		
		a.foodtype = b.foodtype;
		a.consumername = b.consumername;
		a.date = b.date;
		a.quantity = b.quantity;
		a.eatcost =b.eatcost;
		
	}
	
	public static void AscendingSort(ArrayList <Consumer> sample){
		if(sample.size() ==0) { return;
		}
		else{
			// already add size ==1 condition
			if(sample.size() ==1){ return;
				
			}
			else{
				for(int i = sample.size(); i>1; i--){
					for(int j = 0; j<i-1; j++){
						if(! Function.DateCompare(sample.get(j).date,sample.get(j+1).date)){
							Consumer temp = new Consumer(sample.get(j+1));
							Consumer.copy(sample.get(j+1), sample.get(j));
							Consumer.copy(sample.get(j), temp);
						}	
					}
				}
			}
		}
		
	}

}
