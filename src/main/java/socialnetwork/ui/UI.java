package socialnetwork.ui;

import socialnetwork.community.Comunities;
import socialnetwork.domain.Prietenie;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.Utilizator;
import socialnetwork.domain.messages.FriendshipRequest;
import socialnetwork.domain.messages.Message;
import socialnetwork.domain.messages.ReplyMessage;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.service.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UI {
    private UtilizatorService utilizatorService;
    private PrietenieService prietenieService;
    private MessageService messageService;
    private ReplyMessageService replyMessageService;
    private FriendshipRequestService friendshipRequestService;

    public UI(UtilizatorService utilizatorService, PrietenieService prietenieService, MessageService messageService, ReplyMessageService replyMessageService, FriendshipRequestService friendshipRequestService) {
        this.utilizatorService = utilizatorService;
        this.prietenieService = prietenieService;
        this.messageService = messageService;
        this.replyMessageService = replyMessageService;
        this.friendshipRequestService = friendshipRequestService;
    }

    public void run() throws IOException {
        int command = 0;

            while (true) {
                try
                {
                    System.out.println("MENU:");
                    System.out.println("0. EXIT");
                    System.out.println("1. Add a new User");
                    System.out.println("2. Delete User");
                    System.out.println("3. Add a new Friendship");
                    System.out.println("4. Delete Friendship");
                    System.out.println("5. The number of comunities");
                    System.out.println("6. The most soaciable community");
                    System.out.println("7. Print users:");
                    System.out.println("8. Print friendships:");
                    System.out.println("9. The friendships of a user reading from the keyboard:");
                    System.out.println("10. The friendships of a user read from the keyboard, from the month read from the keyboard:");
                    System.out.println("11. Send message to many:");
                    System.out.println("12. Add CONVERASTION:");
                    System.out.println("13. Show conversation between 2 users:");
                    System.out.println("14. Send request friendship: ");
                    System.out.println("15. Respond to a request friendship: ");
                    System.out.print("Introduce comanda: ");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                    command = Integer.parseInt(reader.readLine());
                    switch (command) {
                        case 0:
                            //System.exit(0);
                            return;
                        case 1:
                            this.utilizatorService.addUtilizator(readerUser());
                            System.out.println("Successful add!\n");
                            break;
                        case 2:
                            this.utilizatorService.deleteUtilizator(readIDUserToDelete());
                            System.out.println("Successful delete!\n");
                            break;
                        case 3:
                            this.prietenieService.addPrietenie(readFriends());
                            System.out.println("Successful add!\n");
                            break;
                        case 4:
                            this.prietenieService.deletePrietenie(readIDsPrietenieToDelete());
                            System.out.println("Successful delete!\n");
                            break;
                        case 5:
                            Comunities comunitiesA = new Comunities(prietenieService.getAll());
                            comunitiesA.printNumberofComunities();
                            break;
                        case 6:
                            Comunities comunitiesB = new Comunities(prietenieService.getAll());
                            comunitiesB.printTheMostSociableCommunity();
                            break;
                        case 7:
                            utilizatorService.getAll().forEach(System.out::println);
                            break;
                        case 8:
                            prietenieService.getAll().forEach(System.out::println);
                            break;
                        case 9:
                            afisarePrieteniiUser();
                            break;
                        case 10:
                            afisarePrieteniiUserDinLunaData();
                            break;
                        case 11:
                            sendMessageToMany();
                            break;
                        case 12:
                            addConversation();
                            break;
                        case 13:
                            showConversation();
                            break;
                        case 14:
                            sendFrienshipRequest();
                            break;
                        case 15:
                            respondFriendshipRequest();
                            break;

                    }
                }catch (ValidationException e){
                        System.out.println(e.getMessage());
                }
            }
    }

    /**
     * read from the keyboard id, first and last name, and create a user
     * @return a user
     * @throws IOException
     */
    private Utilizator readerUser() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String idString;

        System.out.print("Introduce the id of the user: ");
        while(true){
            try{
                idString = bufferedReader.readLine();
                Long.parseLong(idString);
                break;
            }catch (NumberFormatException e){
                System.err.println("ID invalid! Introduce a valid id - a number! ");
            }
        }
        String firstNameUser;
        System.out.print("Introduce the first name of the user: ");
        firstNameUser = bufferedReader.readLine();
        String lastNameUser;
        System.out.print("Introduce the last name of the user: ");
        lastNameUser = bufferedReader.readLine();
        Utilizator user = new Utilizator(firstNameUser,lastNameUser);
        user.setId(Long.parseLong(idString));
        return user;
    }

    /**
     *read from the keyboard a long
     * @return an id - the id of the user to be deleted
     * @throws IOException
     */
    private long readIDUserToDelete() throws IOException {
        long userIDToDelete = 0;
        System.out.print("Introduce the ID of the user to be deleted ");
        String idString;
        while (true) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            idString = bufferedReader.readLine();
            try {
                userIDToDelete = Long.parseLong(idString);
                break;
            }catch (NumberFormatException e) {
                System.err.println("Introduce a valid id - a number!");
            }
        }
        return userIDToDelete;
    }

    /**
     *read 2 keyboard ids (long)
     * @return a friendship
     * @throws IOException
     */
    private Prietenie readFriends() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String leftIDUser;
        long leftIDLong = 0;
        System.out.print("Introduce the id of the first user: ");
        while(true){
            leftIDUser = bufferedReader.readLine();
            try{
                leftIDLong = Long.parseLong(leftIDUser);
                break;
            }catch(NumberFormatException e){
                System.err.println("ID invalid! Introduce a valid id - a number! ");
            }
        }

        String rightIDUser;
        long rightIDLong = 0;
        System.out.print("Introduce the id of the second user: ");
        while(true){
            rightIDUser = bufferedReader.readLine();
            try{
                rightIDLong = Long.parseLong(rightIDUser);
                break;
            }catch(NumberFormatException e){
                System.err.println("ID invalid! Introduce a valid id - a number! ");
            }
        }
        Prietenie prietenie = new Prietenie(new Tuple<>(leftIDLong,rightIDLong));
        return prietenie;
    }

    /**
     *read 2 keyboard ids (long)
     * @return Tuple<Long, Long>
     * @throws IOException
     */
    private Tuple<Long, Long> readIDsPrietenieToDelete() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String leftIDUser;
        long leftIDLong = 0;
        System.out.print("Introduce the id of the first user: ");
        while(true){
            leftIDUser = bufferedReader.readLine();
            try{
                leftIDLong = Long.parseLong(leftIDUser);
                break;
            }catch(NumberFormatException e){
                System.err.println("ID invalid! Introduce a valid id - a number! ");
            }
        }

        String rightIDUser;
        long rightIDLong = 0;
        System.out.print("Introduce the id of the second user: ");
        while(true){
            rightIDUser = bufferedReader.readLine();
            try{
                rightIDLong = Long.parseLong(rightIDUser);
                break;
            }catch(NumberFormatException e){
                System.err.println("ID invalid! Introduce a valid id - a number! ");
            }
        }
        return new Tuple<>(leftIDLong,rightIDLong);
    }

    /**
     * read a user's ID and display the friends list
     */
    private void afisarePrieteniiUser(){
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String idString;

        System.out.print("Introduce the id of the user: ");
        while(true){
            try{
                idString = bufferedReader.readLine();
                Long.parseLong(idString);
                break;
            }catch (NumberFormatException | IOException e){
                System.err.println("ID invalid! Introduce a valid id - a number! ");
            }
        }
        Long idUser = Long.parseLong(idString);
        List<Prietenie> listPrieteni = new ArrayList<Prietenie>();
        Iterable<Prietenie> prieteniiIterabile = prietenieService.getAll();
        prieteniiIterabile.forEach(listPrieteni::add);
//        for(Prietenie prietenie : prieteniiIterabile){
//            listPrieteni.add(prietenie);
//        }

        listPrieteni.stream().
                filter(prietenie -> {
                    return prietenie.getId().getLeft().equals(idUser) || prietenie.getId().getRight().equals(idUser);
                })
                .forEach(x-> {
                    Utilizator utilizator = null;
                    if(x.getId().getRight().equals(idUser)){
                        utilizator = utilizatorService.findOne(x.getId().getLeft());
                    }else{
                        utilizator = utilizatorService.findOne(x.getId().getRight());
                    }
                    System.out.println(utilizator.getFirstName()+" | "+utilizator.getLastName()+" | "+x.getDate());

                });
    }

    /**
     * read a user's id, a month and display the list of friends
     */
    private void afisarePrieteniiUserDinLunaData(){
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String idString;

        System.out.print("Introduce the id of the user: ");
        while(true){
            try{
                idString = bufferedReader.readLine();
                Long.parseLong(idString);
                break;
            }catch (NumberFormatException | IOException e){
                System.err.println("ID invalid! Introduce a valid id - a number! ");
            }
        }
        Long idUser = Long.parseLong(idString);
        System.out.print("Introduce the month: ");
        String month;
        while(true){
            try{
                month = bufferedReader.readLine();
                Long.parseLong(month);
                break;
            }catch (NumberFormatException | IOException e){
                System.err.println("ID month! Introduce a number between 01 and 12 ");
            }
        }

        List<Prietenie> listPrieteni = new ArrayList<Prietenie>();
        Iterable<Prietenie> prieteniiIterabile = prietenieService.getAll();
        prieteniiIterabile.forEach(listPrieteni::add);
        String finalMonth = month;
        listPrieteni.stream().
                filter(prietenie -> {
                    return prietenie.getId().getLeft().equals(idUser) || prietenie.getId().getRight().equals(idUser);
                })
                .filter(x->{
                  String date = x.getDate().toString();
                  return date.substring(5,7).equals(finalMonth);
                })
                .forEach(x->{
                    Utilizator utilizator = null;
                    if(x.getId().getRight().equals(idUser)){
                        utilizator = utilizatorService.findOne(x.getId().getLeft());
                    }else{
                        utilizator = utilizatorService.findOne(x.getId().getRight());
                    }
                    System.out.println(utilizator.getFirstName()+" | "+utilizator.getLastName()+" | "+x.getDate());

                });
    }

    /**
     * reads a user ID, a message, and a list of users to whom to send the message
     * @throws IOException
     */
    private void sendMessageToMany() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        //citire id-ul userului care trimite mesajul

        String idString;
        System.out.print("Introduce the id of the user: ");
        while(true){
            try{
                idString = bufferedReader.readLine();
                Long.parseLong(idString);
                break;
            }catch (NumberFormatException | IOException e){
                System.err.println("ID invalid! Introduce a valid id - a number! ");
            }
        }
        Long idUserFrom = Long.parseLong(idString);

        //citire mesaj trimis
        String mesaj;
        System.out.print("Introduce the message: ");
        mesaj = bufferedReader.readLine();

        LocalDateTime data = LocalDateTime.now();

        //lista de useri cui trimitem
        List<Utilizator> listTo =new ArrayList<>();
        System.out.println("Introduce the ids of users to whom the messages is sent. To end the process, introduce 0: ");
        String idUserString = "";
        while(true) {
            System.out.print("Introduce the id: ");
            while (true) {
                try {
                    idUserString = bufferedReader.readLine();
                    Long.parseLong(idUserString);
                    break;
                } catch (NumberFormatException | IOException e) {
                    System.err.println("ID invalid! Introduce a valid id - a number! ");
                }
            }

            Long idd = Long.parseLong(idUserString);
            if(idd == 0)
                break;
            listTo.add(utilizatorService.findOne(idd));
        }

        Utilizator userFrom = utilizatorService.findOne(idUserFrom);
        Message mesajCreat = new Message(userFrom,listTo,mesaj,data);
        messageService.addMessage(mesajCreat);
    }

    /**
     * reads a long id before displaying a specific message
     * @param mesaj - to know what id you enter
     * @return a Long
     */
    private Long readNumberLong(String mesaj){
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String idString;

        System.out.print(mesaj);
        while(true){
            try{
                idString = bufferedReader.readLine();
                Long.parseLong(idString);
                break;
            }catch (NumberFormatException | IOException e){
                System.err.println("ID invalid! Introduce a valid id - a number! ");
            }
        }
        return Long.parseLong(idString);
    }

    /**
     * read 2 ids, a message and simulate a conversation
     * @throws IOException
     */
    private void addConversation() throws IOException {
        Long idUserEmitator = readNumberLong("Introduce the id of the sender: ");
        Long idUserReceptor= readNumberLong("Introduce the id of the receiver: ");
        Utilizator sender = utilizatorService.findOne(idUserEmitator);
        Utilizator receiver = utilizatorService.findOne(idUserReceptor);

        System.out.println("The message: ");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String mesaj = bufferedReader.readLine();

        ReplyMessage replyMessage = new ReplyMessage(sender, Arrays.asList(receiver),mesaj,LocalDateTime.now(),
                replyMessageService.getReplyMessage(0L));

        replyMessageService.addMessage(replyMessage);

        while(true){
            System.out.print("Do you want to continue the conversation? [y/n]");
            System.out.print("Response: ");
            String raspuns = bufferedReader.readLine();
            if(raspuns.equals("y") || raspuns.equals("Y")){
                Utilizator aux = sender;
                sender = receiver;
                receiver = aux;
                System.out.println("Introduce the text message: ");
                String text = bufferedReader.readLine();
                replyMessage = new ReplyMessage(sender,Arrays.asList(receiver),text,LocalDateTime.now(),
                        replyMessageService.getReplyMessage(replyMessage.getId()));
                replyMessageService.addMessage(replyMessage);
            }
            else if(raspuns.equals("n") || raspuns.equals("N")){
                break;
            }
        }
    }

    /**
     * read 2 ids and the conversation between the 2 is displayed
     */
    private void showConversation(){
        Long idUserEmitator = readNumberLong("Introduce the id of the sender: ");
        Long idUserReceptor= readNumberLong("Introduce the id of the receiver: ");
        Iterable<ReplyMessage> conversation = replyMessageService.getConversation(idUserEmitator,idUserReceptor);
        if(!conversation.iterator().hasNext())
            System.out.println("There are no conversations between the two");
        else
        conversation.forEach(System.out::println);
    }

    /**
     * read 2 ids, the first one sends a friend request to the second one
     * @throws IOException
     */
    private void sendFrienshipRequest() throws IOException {
        Long idUserEmitator = readNumberLong("Introduce the id of the sender: ");
        Long idUserReceptor= readNumberLong("Introduce the id of the receiver: ");
        Utilizator sender = utilizatorService.findOne(idUserEmitator);
        Utilizator receiver = utilizatorService.findOne(idUserReceptor);

        System.out.println("The message: ");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String mesaj = bufferedReader.readLine();
        FriendshipRequest request = new FriendshipRequest(sender,Arrays.asList(receiver),mesaj,LocalDateTime.now(),"pending");
        friendshipRequestService.addRequest(request);
    }

    /**
     * a friend request is answered
     * @throws IOException
     */
    private void respondFriendshipRequest() throws IOException {
        Long id = readNumberLong("Introduce the id of the user: ");
        Iterable<FriendshipRequest> friendshipRequestIterable = friendshipRequestService.getAllPendingRequest(id);
        if(!friendshipRequestIterable.iterator().hasNext()){
            System.out.println("There are no pending friendship request!\n");
            return;
        }

        friendshipRequestIterable.forEach(System.out::println);

        Long idFriendshipRequest = readNumberLong("Choose an id for a friendship request: ");
        System.out.print("Approve or Reject?");
        System.out.print("  Choose: a/r  >>");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String choice = bufferedReader.readLine();

        FriendshipRequest friendshipRequest = friendshipRequestService.getOne(idFriendshipRequest);

        if(choice.equals("a") || choice.equals("A")){

            friendshipRequestService.deleteRequest(idFriendshipRequest);
            friendshipRequest.setStatus("approved");
            friendshipRequestService.addRequest(friendshipRequest);

            Long idUserFrom = friendshipRequest.getFrom().getId();
            Prietenie prietenie = new Prietenie(new Tuple<Long,Long>(idUserFrom,id));
            prietenieService.addPrietenie(prietenie);
            System.out.println("Approved succesful!");
        }
        else
        if(choice.equals("r") || choice.equals("R")){
            friendshipRequestService.deleteRequest(idFriendshipRequest);
            friendshipRequest.setStatus("rejected");
            friendshipRequestService.addRequest(friendshipRequest);
            System.out.println("Rejected succesful!");
        }
        else{
            System.out.println("No action");
        }

    }
}
