package main;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.LongBinaryOperator;


import com.prompt.DBKeyTextPane;

public class TextColor {
	private DBKeyTextPane editSql;

	private static Object o = new Object();
	public static void main(String[] args) {
		String sql = 
		"select y1 年,m1 月,c1 本月销售额,                                                                                            "+
		"       c2 上月销售额,                                                                                                        "+
		"       case when c2 is null or c2=0 then '无穷大'                                                                            "+
		"       else cast(cast((isnull(c1, 0)-isnull(c2,0))*100/isnull(c2, 0) as decimal(10,2)) as varchar(50))+'%' end as 环比增长 , "+
		"       c3 去年本月销售额,                                                                                                    "+
		"       case when c3 is null or c3=0 then '无穷大'                                                                            "+
		"       else cast(cast((isnull(c1, 0)-isnull(c3,0))*100/isnull(c3, 0) as decimal(10,2)) as varchar(50))+'%' end as 同比增长   "+
		"from                                                                                                                         "+
		"(                                                                                                                            "+
		"select y1,m1,c1,c2,c3                                                                                                        "+
		"from                                                                                                                         "+
		"(                                                                                                                            "+
		"   select y1,m1,c1,c2 from                                                                                                   "+
		"   (                                                                                                                         "+
		"    select y1,m1,sum(Amt) as c1 from                                                                                         "+
		"     (                                                                                                                       "+
		"          select datepart(year,convert(DateTime,s_date)) as y1,                                                              "+
		"                   datepart(month,convert(DateTime,s_date)) as m1 ,                                                          "+
		"                  Amt                                                                                                        "+
		"         from orders                                                                                                         "+
		"     )  as T1                                                                                                                "+
		"     group by T1.y1,T1.m1                                                                                                    "+
		"    ) o2                                                                                                                     "+
		"                                                                                                                             "+
		"left join                                                                                                                    "+
		"   (                                                                                                                         "+
		"     select y2,m2,sum(Amt) as c2 from                                                                                        "+
		"     (                                                                                                                       "+
		"         select datepart(year,convert(DateTime,s_date)) as y2,                                                               "+
		"                  datepart(month,convert(DateTime,s_date)) as m2 ,                                                           "+
		"                  Amt from orders                                                                                            "+
		"       )  as T1                                                                                                              "+
		"       group by T1.y2,T1.m2                                                                                                  "+
		"     ) o3                                                                                                                    "+
		"   on o2.y1 = o3.y2 and o2.m1 = o3.m2 - 1                                                                                    "+
		"  ) as o4                                                                                                                    "+
		"  left join                                                                                                                  "+
		"  (                                                                                                                          "+
		"     select y3,m3,sum(Amt) as c3 from                                                                                        "+
		"     (                                                                                                                       "+
		"         select datepart(year,convert(DateTime,s_date)) as y3,                                                               "+
		"                  datepart(month,convert(DateTime,s_date)) as m3,                                                            "+
		"                  Amt from orders                                                                                            "+
		"      )  as T1                                                                                                               "+
		"      group by T1.y3,T1.m3                                                                                                   "+
		"   ) as o5                                                                                                                   "+
		"   on o4.y1 = o5.y3 - 1 and o4.m1 = o5.m3                                                                                    "+
		") as o6                                                                                                                      ";
		String upperCase = sql.toUpperCase();
		
		String[] split = upperCase.split("FROM");
		int start = upperCase.indexOf("SELECT")+6;
		int fr = 0;
		for (int i = 0; i < split.length; i++) {
			fr = upperCase.indexOf("FROM",start);
			String[] split2 = upperCase.substring(start, fr).split("FROM");
			if(split2.length>1){
				start = fr+4;
			}else{
				break;
			}
			if(upperCase.indexOf("FROM",start)==-1){
				break;
			}
		}
		String substring = upperCase.substring(upperCase.indexOf("SELECT")+6, fr);
		System.out.println(upperCase.replace(substring, " count(1) "));
//		try {
//			new TextColor().init();
//		} catch (UnsupportedFlavorException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	public void init() throws UnsupportedFlavorException, IOException {
		AtomicLong a = new AtomicLong();
		Thread th = new Thread(){
			public void run() {
				int i = 0;
				while(true){
					if(i++>=10){
						break;
					}
					a.accumulateAndGet(1, new LongBinaryOperator() {
						@Override
						public long applyAsLong(long left, long right) {
							return left+right;
						}
					});
					System.out.println(a);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			};
		};
		Thread th1 = new Thread(){
			public void run() {
				int i = 0;
				while(true){
					if(i++>=10){
						break;
					}
					a.accumulateAndGet(1, new LongBinaryOperator() {
						@Override
						public long applyAsLong(long left, long right) {
							return left+right;
						}
					});
					System.out.println(a);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			};
		};
		th.start();
		th1.start();
		System.out.println(a);
	}
	
	public void set(){
		TreeSet<A> a = new TreeSet<>(new Comparator<A>() {

			@Override
			public int compare(A v1, A v2) {

				String[] split = v1.getA().split("\\.");
				String[] split2 = v2.getA().split("\\.");
				if(split.length>1){
					if(split2.length>1){
						if(split[1].equals((split2[1]))){
	
							return 0;
						}
					}else{
						if( split[1].equals(split2[0])){
							return 0;
						}
					}
				}else{
					if(split2.length>1){
						if(split[0].equals(split2[1])){
							return 0;	
						}
					}else{
						if(split[0].equals(split2[0])){
							return 0;	
						}
					}
				}

			return -1;
			
			}
		});
		a.add(new A("1.1","1321"));
		a.add(new A("2.1","1321"));
		a.add(new A("3.1","1321"));
		boolean add = a.add(new A("1","1"));
		System.out.println(add);
		
	}
	
}
class A {
	private String a;
	public A(String a ,String b){
		this.setA(a);
	}
	public String getA() {
		return a;
	}
	public void setA(String a) {
		this.a = a;
	}
	
	@Override
	public boolean equals(Object obj) {

		A v =(A) obj;
		String[] split = v.getA().split("\\.");
		String[] split2 = this.getA().split("\\.");
//		if(!v.getKey().equals(key)){
//			return split[1].equals(split2[1]);
//		}
		if(split.length>1){
			if(split2.length>1){
				return split[1].equals(split2[1]);
			}else{
				return split[1].equals(split2[0]);
			}
		}else{
			if(split2.length>1){
				return split[0].equals(split2[1]);
			}else{
				return split[0].equals(split2[0]);
			}
		}
	}
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 1;
	}
	
}


