package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import it.polito.tdp.crimes.model.Event;
import it.polito.tdp.crimes.model.Giorno;


public class EventsDao {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<String> getAllCategory(){
		String sql="SELECT DISTINCT(offense_category_id) AS categorie " + 
				"from EVENTS " + 
				"ORDER BY categorie asc ";
		
		List<String> result= new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;

			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add(res.getString("categorie"));
			}
			
			conn.close();
			return result;
			
		}catch (SQLException e) {
			
			e.printStackTrace();
			return null ;
		}
	}
	
	
	public List<LocalDate> getAllDay(){
		String sql="SELECT DISTINCT(DATE(reported_date)) as data " + 
				"from EVENTS " + 
				"ORDER BY reported_date asc";
		
		List<LocalDate> result= new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;

			ResultSet res = st.executeQuery() ;
		
			while(res.next()) {
				result.add(res.getDate("data").toLocalDate());
			} 
			
			conn.close();
			return result;
			
		}catch (SQLException e) {
			
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<String> getTipo(String categoria, LocalDate giorno){
		String sql="SELECT offense_type_id AS tipo " + 
				"from EVENTS " + 
				"WHERE offense_category_id=? AND YEAR(reported_date)=? AND MONTH(reported_date)=? AND DAY(reported_date)=? ";
	
		List<String> result= new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			st.setString(1, categoria);
			st.setInt(2, giorno.getYear());
			st.setInt(3, giorno.getMonthValue());
			st.setInt(4, giorno.getDayOfMonth());
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add(res.getString("tipo"));
			}
			
			conn.close();
			return result;
			
		}catch (SQLException e) {
			
			e.printStackTrace();
			return null ;
		}
	
	}
	
	public List<Adiacenza> getAdiacenze(String categoria, LocalDate giorno){
		String sql="SELECT e1.offense_type_id AS tipo1, e2.offense_type_id AS tipo2, COUNT(DISTINCT e1.precinct_id) AS peso " + 
				"from EVENTS e1, EVENTS e2 " + 
				"WHERE e1.offense_type_id > e2.offense_type_id AND e1.precinct_id=e2.precinct_id AND e1.offense_category_id=e2.offense_category_id AND " + 
				"		e1.offense_category_id=? AND DATE(e1.reported_date)=DATE(e2.reported_date) AND " + 
				"		YEAR(e1.reported_date)=? AND " + 
				"		MONTH(e1.reported_date)=? AND " + 
				"		DAY(e1.reported_date)=? " + 
				"GROUP BY  e1.offense_type_id, e2.offense_type_id";
		List<Adiacenza> result = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			st.setString(1, categoria);
			st.setInt(2, giorno.getYear());
			st.setInt(3, giorno.getMonthValue());
			st.setInt(4, giorno.getDayOfMonth());
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add(new Adiacenza(res.getString("tipo1"),
										 res.getString("tipo2"),
										 res.getInt("peso")) );
			}
			
			conn.close();
			return result;
			
		}catch (SQLException e) {
			
			e.printStackTrace();
			return null ;
		}
	
	}

}
