import java.util.*;
import java.io.*;

public class Main {
  private static char[] letters;
  private static String abc = "Z]E4*v7Ty0}6jBepAC5w(;d<J^8szO.V|h\\_Ml,$`?{n%ufU[\"S=9W@xt2:K~)b>oi3rQ'cYH#ag!DI&/k+XPRmNqFGL-1";
  private static String chars = "!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
  private static char[] charsArray = chars.toCharArray();
  private static char[] abcArray = abc.toCharArray();
  final static Scanner sc = new Scanner(System.in);
  final static File DATABASE = new File("src/main/java/database.txt");
  final static List<Information> informations = readData();
  
  public static void main(String[] args) {
    while(true){
      System.out.println("******************************");
      System.out.print("(R)egister\n(L)og In\nChoice: ");
      String options = sc.nextLine();
      
      if(options.toUpperCase().equals("R")){
        register();
      } else if(options.toUpperCase().equals("L")){
        login();
      }
    }
  }
  
  static void register(){
    String username, password;
    while(true){
      System.out.println("******************************");
      System.out.print("Enter a username: ");
      username = sc.nextLine();

      if(username.length() < 6){
        System.out.println("The username length must be atleast 6 characters.");
      } else if(checkUsername(username)){
        System.out.println("The username "+username+" already exist.");
      } else if(username.contains(" ")){
        System.out.println("Username may not contain space");
      } else{
        break;
      }
    }

    while(true){
      System.out.println("******************************");
      System.out.print("Enter password: ");
      password = sc.nextLine();

      if(password.length() < 6){
        System.out.println("The password length must be atleast 6 characters.");
      } else if(password.contains(" ")){
        System.out.println("Password may not contain space");
      } else{
        break;
      }
    }

    informations.add(new Information(username, encrypt(password)));
    updateDatabase();
    System.out.println("Now log in!");
  }

  static void login(){
    String username, password;
    
    while(true){
      System.out.println("******************************");
      System.out.print("Enter a username: ");
      username = sc.nextLine();

      if(!checkUsername(username)){
        System.out.println("The username "+username+" does not exist.");
      }else{
        break;
      }
    }

    password = askPassword(username);

    editInfo(username, password);
  }

  static void editInfo(String username, String password) {
    String choice, newUsername, newPassword;
    do {
      System.out.println("******************************");
      System.out.print("Edit (U)sername\nEdit (P)assword\n(D)elete Account\nChoice: ");
      choice = sc.nextLine();
    } while(!choice.toUpperCase().equals("U") && !choice.toUpperCase().equals("P") && !choice.toUpperCase().equals("D"));

    askPassword(username);
    
    if(choice.toUpperCase().equals("U")){
      while(true){
        System.out.println("******************************");
        System.out.print("Enter new username: ");
        newUsername = sc.nextLine();

        if(newUsername.length() < 6){
          System.out.println("The username length must be atleast 6 characters.");
        } else if(checkUsername(newUsername)){
          System.out.println("The username "+newUsername+" already exist.");
        } else if(newUsername.contains(" ")){
          System.out.println("Username may not contain space");
        } else{
          break;
        }
      } 

      for(Information info : informations){
        if(info.getUserName().equals(username)){
          info.setUserName(newUsername);
          updateDatabase();
        }
      }
    } else if(choice.toUpperCase().equals("P")){

      while(true){
        System.out.println("******************************");
        System.out.print("Enter new password: ");
        newPassword = sc.nextLine();

        if(newPassword.length() < 6){
          System.out.println("The password length must be atleast 6 characters.");
        } else if(newPassword.contains(" ")){
          System.out.println("Password may not contain space");
        } else{
          break;
        }
      }
      
      for(Information info : informations){
        if(info.getUserName().equals(username)){
          info.setPassWord(encrypt(newPassword));
          updateDatabase();
        }
      }
    } else if(choice.toUpperCase().equals("D")){
      for(Iterator<Information> iterator = informations.iterator(); iterator.hasNext(); ){
        Information info = iterator.next();
        if(info.getUserName().equals(username)){
          iterator.remove();
          updateDatabase();
          System.out.println("Account "+username+" has been deleted");
        }
      }
    }
  }

  static List<Information> readData() {
    List<Information> informations = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(DATABASE))) {
      String line;
      while ((line = br.readLine()) != null) {
        String[] parts = line.split(" ");
        String userName = parts[0];
        String passWord = parts[1];
        informations.add(new Information(userName, passWord));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return informations;
  }

  static boolean checkUsername(String userName){
    List<Information> informations = readData();
    for(Information info : informations){
      if(info.getUserName().equals(userName)){
        return true;
      }
    }
    return false;
  }
  
  static String askPassword(String userName){
    String passWord;
    while(true){
      System.out.println("******************************");
      System.out.print("Enter password: ");
      passWord = sc.nextLine();

      if(!checkPassword(userName, encrypt(passWord))){
        System.out.println("Password incorrect!");
      }else{
        break;
      }
    }
    return passWord;
  }

  static boolean checkPassword(String userName, String passWord){
    List<Information> informations = readData();
    for(Information info : informations){
      if(info.getUserName().equals(userName) && info.getPassWord().equals(passWord)){
        return true;
      }
    }
    return false;
  }

  static void updateDatabase() {
    try (PrintWriter pw = new PrintWriter(new FileWriter(DATABASE))) {
      for(Information information: informations) {
        pw.println(information.getUserName() + " " + information.getPassWord());
      }
    } catch (IOException e){
      e.printStackTrace();
    }
  }

  private static String encrypt(String password){
    letters = password.toCharArray();
    
    for(int i = 0; i < letters.length; i++){
      for(int j = 0; j < charsArray.length; j++){
        if(letters[i] == charsArray[j]){
          letters[i] = abcArray[j];
          break;
        }
      }
    }

    password = new String(letters);
    return password; 
  }
}

class Information {
  private String userName;
  private String passWord;

  public Information(String userName, String passWord) {
    this.userName = userName;
    this.passWord = passWord;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }
  
  public String getPassWord(){
    return passWord;
  }

  public void setPassWord(String passWord) {
    this.passWord = passWord;
  }
}