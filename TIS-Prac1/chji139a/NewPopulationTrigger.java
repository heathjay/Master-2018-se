import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.Statement;
import org.h2.api.Trigger;

public class NewPopulationTrigger implements Trigger {

    
    // you only need to change the fire function here
	@Override
	public void fire(Connection arg0, Object[] arg1, Object[] arg2)
			throws SQLException {

		PrepareStatement stmt = arg0.prepareStatement("update country set current_population=?+current_population where country.code =?");
		stmt.setObject(1,arg2[1]);
        stmt.setObject(2,arg2[0]);
		stmt.executeUpdate()!=0;
		// do something
		
		
	}
	
	@Override
	public void init(Connection arg0, String arg1, String arg2, String arg3,
			boolean arg4, int arg5) throws SQLException {
		// TODO Auto-generated method stub
		
	}


	

}


