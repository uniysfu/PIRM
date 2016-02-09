package PIRM15S2;

import java.util.ArrayList;


public class Food {

	public double totalprice;
	public double unitprice;
	public String offdate;
	public double quantity;
	public String type;
	
	
	
	public Food(String name ,String date, double count, double cost ){
		offdate = date;
		totalprice = cost;
		quantity = count;
		unitprice = totalprice/quantity;
		type = name;
	}
	
	public Food(Food a){
		this.totalprice = a.totalprice;
		this.unitprice = a.unitprice;
		this.offdate = a.offdate;
		this.quantity = a.quantity;
		this.type =a.type;
	}
	
	public static void copy(Food a, Food b){
		
		a.totalprice = b.totalprice;
		a.unitprice = b.unitprice;
		a.offdate = b.offdate;
		a.quantity = b.quantity;
		a.type =b.type;
		
	}
	
	public static void Count (ArrayList<Food> a){
		
		int n = a.size();
		
		System.out.println("elements of this arryalist is : "+n);
		
	}
	
	public void ShowValues(){
		
		System.out.println(type+" quantity: "+ quantity +" use-by: "+offdate+" unitprice: "+ unitprice);
	}
}

