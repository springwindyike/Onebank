package app.db;

import java.util.List;
import org.sql2o.*;
import lombok.*;
import com.fasterxml.jackson.annotation.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Data
public class User {
  @JsonIgnore  
  private static final Logger LOGGER = LogManager.getLogger();
    
  private int id;
  private String givenName;
  private String surName;
  private String username;
  private String password;
  private String photolink;
  private String email;
  private String function;
  private boolean jabber_use;
  private boolean spark_use;
  private boolean adminprivilege;
  private String oauthAccessToken = null;
  private String oauthRefreshToken = null;
  
  public static List<User> all(String jtStartIndex, String jtPageSize, String jtSorting) {
    String sql = "SELECT * FROM users ORDER BY " + jtSorting + " LIMIT " + jtPageSize + " OFFSET " + jtStartIndex;
    try(Connection con = DB.sql2o.open()) {
     return con.createQuery(sql).executeAndFetch(User.class);
    }
  }
  
  public static List<User> all() {
    String sql = "SELECT * FROM users";
    try(Connection con = DB.sql2o.open()) {
     return con.createQuery(sql).executeAndFetch(User.class);
    }
  }
   
 public static int getUserCount() {
    String sql = "SELECT COUNT (*) FROM users";
    try(Connection con = DB.sql2o.open()) {
        Integer  count = con.createQuery(sql)
        .executeScalar(Integer.class);
     return count;
    }
  } 
  
  @JsonIgnore
  public String getDisplayName() {
      return givenName + " " + surName;
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
        String sql = "INSERT INTO users(givenName, surName, email, jabber_use, spark_use, adminprivilege, photolink,"
          + "username, password, function, oauthAccessToken, oauthRefreshToken) VALUES (:givenName, :surName, :email, :jabber_use, :spark_use, :adminprivilege,"
          + " :photolink, :username, :password, :function, :oauthAccessToken, :oauthRefreshToken)";
        System.out.println("this.oauthAccessToken : " + this.oauthAccessToken);
        this.id = (int) con.createQuery(sql, true)
        .addParameter("givenName", this.givenName)
        .addParameter("surName", this.surName)
        .addParameter("email", this.email)
        .addParameter("jabber_use", this.jabber_use)
        .addParameter("spark_use", this.spark_use)
        .addParameter("adminprivilege", this.adminprivilege)
        .addParameter("photolink", this.photolink)
        .addParameter("username", this.username)
        .addParameter("password", this.password)
        .addParameter("function", this.function)
        .addParameter("oauthAccessToken", this.oauthAccessToken)
        .addParameter("oauthRefreshToken", this.oauthRefreshToken)
        .executeUpdate()
        .getKey();
        
    } catch (Exception e) {
        LOGGER.error("Error in User save methode" + e);
    }
  }

  public static User find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM users where id=:id";
      User user = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(User.class);
      return user;
    } catch (Exception e) {
        LOGGER.error("Error in User find methode" + e);
    } return null;
  }

  public void update() {
    try(Connection con = DB.sql2o.open()) {
        String sql = "UPDATE users SET givenName = :givenName, surName = :surName, email = :email, jabber_use = :jabber_use,"
                + "spark_use = :spark_use, adminprivilege = :adminprivilege, function = :function,"
                + "username = :username, password= :password";
        if (this.photolink != null) sql = sql + ", photolink = :photolink"; //fix when table is updated and link is null
        sql = sql + " WHERE id = :id";
        Query query = con.createQuery(sql)
          .addParameter("givenName", this.givenName)
          .addParameter("surName", this.surName)
          .addParameter("email", this.email)
          .addParameter("jabber_use", this.jabber_use)
          .addParameter("spark_use", this.spark_use)
          .addParameter("adminprivilege", this.adminprivilege)
          .addParameter("username", this.username)
          .addParameter("password", this.password)
          .addParameter("function", this.function) 
          .addParameter("id", id);
    if (this.photolink != null) query.addParameter("photolink", this.photolink); //fix when table is updated and link is null        
      query.executeUpdate();
    } catch (Exception e) {
        LOGGER.error("Error in User update methode" + e);
    }
  }

    public void updateOauth() {
    try(Connection con = DB.sql2o.open()) {
        String sql = "UPDATE users SET oauthaccesstoken= :oauthaccesstoken, oauthrefreshtoken= :oauthrefreshtoken";
        sql = sql + " WHERE id = :id";
        Query query = con.createQuery(sql)
          .addParameter("oauthaccesstoken", this.oauthAccessToken)
          .addParameter("oauthrefreshtoken", this.oauthRefreshToken)      
          .addParameter("id", id);        
          query.executeUpdate();
    } catch (Exception e) {
        LOGGER.error("Error in updateOauth methode" + e);
    }
  }
  
  public void delete() {
    try(Connection con = DB.sql2o.open()) {
        String sql = "DELETE FROM users WHERE id = :id";
        con.createQuery(sql)
          .addParameter("id", id)
          .executeUpdate();
    } catch (Exception e) {
        LOGGER.error("Error in User delete methode" + e);
    }
  }

  public static User getUserByUsername(String username) {
    try(Connection con = DB.sql2o.open()) {
        String sql = "SELECT * FROM users where username=:username";
        User user = con.createQuery(sql)
        .addParameter("username", username)
        .executeAndFetchFirst(User.class);
        return user;
    } catch (Exception e) {
        LOGGER.error("Error in User find methode" + e);
    } return null;    
  }     
}
