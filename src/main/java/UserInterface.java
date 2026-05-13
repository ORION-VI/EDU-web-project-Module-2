import org.example.dao.DataAccessObject;
import org.example.dao.SessionFactoryProvider;
import org.example.entity.User;
import org.example.service.Service;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class UserInterface {
    static void main(String[] args) throws InterruptedException {
        System.out.println("=".repeat(50));
        System.out.println("EDU PostgreSQL Database Interaction Interface v.0.1");
        System.out.println("=".repeat(50));
        System.out.println();
        System.out.print("INITIALIZING RESOURCES...");
        TimeUnit.SECONDS.sleep(1);
        System.out.print("....");
        TimeUnit.SECONDS.sleep(2);
        System.out.print("...");

        Service service = new Service(new DataAccessObject(SessionFactoryProvider.getSessionFactory()));
        Scanner inputScanner = new Scanner(System.in);

        TimeUnit.SECONDS.sleep(1);
        System.out.print("....DONE!");
        System.out.println();
        System.out.println();

        System.out.println("LOGIN: ");
        String login = inputScanner.nextLine();
        System.out.println();
        System.out.println("WELCOME BACK, " + login);
        System.out.println("=".repeat(50));

        while(true) {
            System.out.println();
            System.out.println("MAIN MENU");
            System.out.println("-".repeat(10));
            System.out.println("PLEASE, SELECT AN OPTION BELOW TO PROCEED...");
            System.out.println();
            System.out.println("1. CREATE A NEW USER");
            System.out.println("2. FIND USER BY ID");
            System.out.println("3. GET ALL USERS INFO");
            System.out.println("4. UPDATE USER INFO");
            System.out.println("5. DELETE USER");
            System.out.println("6. EXIT");

            if(!inputScanner.hasNextInt()) {
                System.out.println("INVALID INPUT, SELECT A CORRESPONDING OPTION");
                inputScanner.nextLine();
                continue;
            }

            int selection = inputScanner.nextInt();
            inputScanner.nextLine();
            switch(selection) {

                case 1:
                    System.out.println();
                    System.out.println("CREATING NEW USER");
                    System.out.println("-".repeat(10));
                    System.out.println();
                    System.out.println("ENTER USER NAME: ");
                    String newName = inputScanner.nextLine();
                    System.out.println("ENTER USER EMAIL: ");
                    String newEmail = inputScanner.nextLine();
                    System.out.println("ENTER USER AGE: ");
                    if(!inputScanner.hasNextInt()) {
                        System.out.println("AGE MUST BE A NUMBER!");
                        break;
                    }
                    Integer newAge = inputScanner.nextInt();
                    inputScanner.nextLine();
                    service.saveUser(newName, newEmail, newAge);
                    break;

                case 2:
                    System.out.println();
                    System.out.println("FINDING USER BY ID");
                    System.out.println("-".repeat(10));
                    System.out.println();
                    System.out.println("ENTER USER ID: ");
                    if(inputScanner.hasNextLong()) {
                        Long lookupId = inputScanner.nextLong();
                        inputScanner.nextLine();
                        User user = service.findUser(lookupId);
                        if(user != null) {
                            System.out.println(user);
                            break;
                        }
                        break;
                    }
                    System.out.println("INVALID USER ID!");
                    inputScanner.nextLine();
                    break;

                case 3:
                    System.out.println();
                    System.out.println("GETTING ALL USERS INFO");
                    System.out.println("-".repeat(10));
                    System.out.println();
                    List<User> userList = service.findAllUsers();
                    if(!userList.isEmpty()) {
                        for(User user : userList) {
                            System.out.println(user);
                            System.out.println();
                        }
                        break;
                    }
                    break;

                case 4:
                    System.out.println();
                    System.out.println("UPDATING USER INFO");
                    System.out.println("-".repeat(10));
                    System.out.println();
                    System.out.println("ENTER USER ID: ");
                    if(!inputScanner.hasNextLong()) {
                        System.out.println("INVALID USER ID!");
                        inputScanner.nextLine();
                        break;
                    }
                    Long updateId = inputScanner.nextLong();
                    inputScanner.nextLine();
                    System.out.println("ENTER USER NAME: ");
                    String updateName = inputScanner.nextLine();
                    System.out.println("ENTER USER EMAIL: ");
                    String updateEmail = inputScanner.nextLine();
                    System.out.println("ENTER USER AGE: ");
                    if(!inputScanner.hasNextInt()) {
                        System.out.println("AGE MUST BE A NUMBER!");
                        break;
                    }
                    Integer updateAge = inputScanner.nextInt();
                    inputScanner.nextLine();
                    service.updateUser(updateId, updateName, updateEmail, updateAge);
                    break;

                case 5:
                    System.out.println();
                    System.out.println("DELETING USER");
                    System.out.println("-".repeat(10));
                    System.out.println();
                    System.out.println("ENTER USER ID: ");
                    if(inputScanner.hasNextLong()) {
                        Long deleteId = inputScanner.nextLong();
                        inputScanner.nextLine();
                        service.deleteUser(deleteId);
                        break;
                    }
                    System.out.println("INVALID USER ID!");
                    inputScanner.nextLine();
                    break;

                case 6:
                    System.out.println();
                    System.out.println("EXITING PROGRAM...");
                    SessionFactoryProvider.sessionFactoryShutdown();
                    inputScanner.close();
                    System.out.println("HAVE A NICE DAY!");
                    System.exit(0);

                default:
                    System.out.println("INVALID INPUT, SELECT A CORRESPONDING OPTION");
                    inputScanner.nextLine();
            }
        }
    }
}
