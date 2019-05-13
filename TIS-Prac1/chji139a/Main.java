import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


public class Main {

    
    // you only need to change the main function here.
	public static void main(String[] args) throws Exception {
//        Class.forName("org.h2.Driver");
//        
//        String DBurl = "jdbc:h2:tcp://localhost:9092/~/test";
//        
//        if (args != null && args.length > 0) DBurl = args[0];
//        
//        Connection conn = DriverManager.getConnection(DBurl, "sa", "");
           
//      DatabaseMetaData dbmd = conn.getMetaData();
//    	System.out.println("Database system: " 	+ dbmd.getDatabaseProductName());
//    	System.out.println("Version        : "	+ dbmd.getDatabaseMajorVersion() + "." + dbmd.getDatabaseMinorVersion());
//   
//    	
        // lets have a look on all tables
// 
//    	ResultSet rs = dbmd.getTables(null, null, "%", null);
//    	while (rs.next()) {
//            System.out.println(rs.getString(3));
//    	}
//    	rs.close();
//    	
//    	// do something else ...
// 
//        conn.close();
        
        
///*************************PARTII**************************//

		
		String DB_URL = "jdbc:h2:tcp://localhost:9092/~/test";        
		String SQL_COM1 = 
				"Create table if not exists newpopulation(country_code varchar(4) not null default '',amount int not null default 0)";
		String SQL_COM2 = 
				"alter table country add column current_population int";
		String SQL_COM3 = 
				"insert into newpopulation (country_code,amount) values(?,?)";
		String SQL_COM4 =
				"UPDATE COUNTRY C SET CURRENT_POPULATION=  POPULATION+ IFNULL((SELECT SUM(AMOUNT) FROM NEWPOPULATION N WHERE C.CODE=N.COUNTRY_CODE ),0)";
		
		if(Class.forName("org.h2.Driver")!=null) {
			System.out.println("connected successfully");		
			if(args!= null && args.length >0 ) 
				DB_URL = args[0];		
			Connection con = DriverManager.getConnection(DB_URL,"sa", "");
			DatabaseMetaData dbmd=con.getMetaData();
			
			
			
			
//			/***********create table**********/
			ResultSet condition = dbmd.getTables(null, null, "newpopulation", null);
			if(condition.next()==false) {
			Statement stmt = con.createStatement();
			int rs1 = stmt.executeUpdate(SQL_COM1);
			System.out.println("table is created sucessfully");
			if(stmt!=null)stmt.close();
			}else System.out.println("table has been created");
			
//			/***********add column**********/
			condition =dbmd.getColumns(null, null, "country", "current_population");
			if(condition.next()==false) {
			Statement stmt = con.createStatement();	
			int rs2 = stmt.executeUpdate(SQL_COM2);
			System.out.println("country is upated");
			if(stmt!=null)stmt.close();
			}else System.out.println("country has been updated");
				
///********prepared statement********/	
//			/***********insert tuples**********/
			PreparedStatement pstmt = con.prepareStatement(SQL_COM3);
			ParameterMetaData md = pstmt.getParameterMetaData();
			int count = 100;
//		
			Object value[]=new Object[] {"NZ",5};
			for(int i =0; i<count; i++) {
				pstmt.setObject(1, value[0]);
				pstmt.setObject(2, value[1]);
				pstmt.executeUpdate();
			}
			if(pstmt !=null)
				pstmt.close();
//			
//			
//			/***********update country**********/
			Statement stmt = con.createStatement();
			int rs4 = stmt.executeUpdate(SQL_COM4);
			System.out.println("country_population is updated sucessfully");
			if(stmt!=null)stmt.close();
			
			
			
			
/***********close the connection******************/
			if(con !=null)
				con.close();
			
		}else System.out.println("connection error");

		
		
		
	}
	
}


