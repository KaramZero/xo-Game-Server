/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;


public class PlayerModel {
   
    private String userName;
    private String email;
    private String name;
    private int score;
    private String password;
    public PlayerModel(){}

    public PlayerModel(String userName, String email, String name, int score, String password) {
        this.userName = userName;
        this.email = email;
        this.name = name;
        this.score = score;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public String getPassword() {
        return password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setPassword(String password) {
        this.password = password;
    }
   
    
    
    
}
