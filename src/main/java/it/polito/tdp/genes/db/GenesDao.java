package it.polito.tdp.genes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.genes.model.Adiacenza;
import it.polito.tdp.genes.model.Interactions;


public class GenesDao {
	
//	public List<Adiacenza> getAllGenes(){
//		String sql = "SELECT DISTINCT GeneID, Essential, Chromosome FROM Genes";
//		List<Adiacenza> result = new ArrayList<Adiacenza>();
//		Connection conn = DBConnect.getConnection();
//
//		try {
//			PreparedStatement st = conn.prepareStatement(sql);
//			ResultSet res = st.executeQuery();
//			while (res.next()) {
//
//				Adiacenza genes = new Adiacenza(res.getString("GeneID"), 
//						res.getString("Essential"), 
//						res.getInt("Chromosome"));
//				result.add(genes);
//			}
//			res.close();
//			st.close();
//			conn.close();
//			return result;
//			
//		} catch (SQLException e) {
//			throw new RuntimeException("Database error", e) ;
//		}
//	}
	
	public List<Integer> getCromosomi(){
		String sql = "SELECT distinct g.Chromosome "
				+ "FROM genes g "
				+ "WHERE g.Chromosome>=1 ";
		List<Integer> result = new ArrayList<Integer>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.add(res.getInt("g.Chromosome"));
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			throw new RuntimeException("Database error", e) ;
		}
	}
	
	public List<Adiacenza> getArco(){
		
		String sql = "SELECT T.c1,T.c2, SUM(I.Expression_Corr) AS peso "
				+ "FROM (SELECT distinct  t1.Chromosome AS c1, t2.Chromosome AS c2, t1.GeneID AS geneC1, t2.GeneID AS geneC2 "
				+ "FROM (SELECT distinct g.Chromosome, g.GeneID "
				+ "FROM genes g "
				+ "WHERE g.Chromosome>=1) AS t1, (SELECT distinct g.Chromosome, g.GeneID "
				+ "FROM genes g "
				+ "WHERE g.Chromosome>=1) AS t2 "
				+ "WHERE t1.Chromosome<>t2.Chromosome AND "
				+ "(t1.GeneID,t2.GeneID) IN (SELECT i.GeneID1,i.GeneID2 "
				+ "FROM interactions i) "
				+ "ORDER BY c1,c2) AS T, interactions I "
				+ "WHERE I.GeneID1=T.geneC1 AND I.GeneID2=geneC2 "
				+ "GROUP BY T.c1,T.c2 "
				+ "ORDER BY T.c1,T.c2";
		List<Adiacenza> result = new ArrayList<Adiacenza>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Adiacenza arco = new Adiacenza(res.getInt("T.c1"), 
						res.getInt("T.c2"), 
						res.getDouble("peso"));
				result.add(arco);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			throw new RuntimeException("Database error", e) ;
		}
	}


	
}
