package main;

public class PARTITION {

	public static void main(String[] args) {
		String a ="[\"java.util.HashMap\","+
				"{"+
				"\"bindEmail\":null,"+
				"\"endAcAmt\":null,"+
				"\"versionOptimizedLock\":0,"+
				"\"endValue\":null,"+
				"\"accountAmt\":[\"java.math.BigDecimal\","+
				"0],"+
				"\"pw\":null,"+
				"\"startAcAmt\":null,"+
				"\"freezeTime\":null,"+
				"\"createdOn\":[\"java.sql.Timestamp\","+
				"1492506692257],"+
				"\"partition\":null,"+
				"\"merchantId\":[\"java.lang.Long\","+
				"2400],"+
				"\"onwayAmt\":[\"java.math.BigDecimal\","+
				"0],"+
				"\"startTime\":null,"+
				"\"id\":[\"java.lang.Long\","+
				"8099433],"+
				"\"startValue\":null,"+
				"\"accountRole\":null,"+
				"\"totalInvest\":null,"+
				"\"totalSubsidyAmt\":[\"java.math.BigDecimal\","+
				"0],"+
				"\"credentialsNo\":null,"+
				"\"salt\":\"OoqEJ2Bx\","+
				"\"lastIP\":null,"+
				"\"accountType\":null,"+
				"\"pageInfo\":null,"+
				"\"updatedOn\":[\"java.sql.Timestamp\","+
				"1492506692257],"+
				"\"userName\":\"A44B03E4C91737651B19C180D54ED88F@qq.sohu.com\","+
				"\"withdrawAmt\":[\"java.math.BigDecimal\","+
				"0],"+
				"\"bindMobile\":null,"+
				"\"lastLoginTime\":null,"+
				"\"realName\":null,"+
				"\"endTime\":null,"+
				"\"fields\":null,"+
				"\"verityLevel\":null,"+
				"\"status\":[\"com.sohu.wallet.enums.AccountStatus\","+
				"\"NORMAL\"],"+
				"\"status_s\":null"+
				"}]";
		
		byte[] bytes = a.getBytes();
		System.out.println(bytes.length);
	}
}
