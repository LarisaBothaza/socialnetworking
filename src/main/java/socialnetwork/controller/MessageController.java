package socialnetwork.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import socialnetwork.domain.Prietenie;
import socialnetwork.domain.UserDTO;
import socialnetwork.domain.Utilizator;
import socialnetwork.domain.messages.Message;
import socialnetwork.service.MessageService;
import socialnetwork.service.PrietenieService;
import socialnetwork.service.UtilizatorService;
import socialnetwork.utils.observer.Observable;



import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static jdk.internal.net.http.common.Utils.close;

public class MessageController {
    ObservableList<UserDTO> modelUnselected = FXCollections.observableArrayList();
    ObservableList<UserDTO> modelSelected = FXCollections.observableArrayList();
    ObservableList<Message> modelInbox = FXCollections.observableArrayList();

    UtilizatorService utilizatorService;
    MessageService messageService;
    PrietenieService prietenieService;
    UserDTO selectedUserDTO;

    List<UserDTO> listUsersSelected= new ArrayList<>();
    List<UserDTO> listUsersUnselected = new ArrayList<>();

    @FXML
    Label labelUserCompose;

    @FXML
    Label labelUserInbox;

    @FXML
    TableView<UserDTO> tableViewUnselected;

    @FXML
    TableColumn<UserDTO, String> tableColumnFirstNameUnselected;

    @FXML
    TableColumn<UserDTO, String> tableColumnLastNameUnselected;

    @FXML
    TableView<UserDTO> tableViewSelected;

    @FXML
    TableColumn<UserDTO, String> tableColumnFirstNameSelected;

    @FXML
    TableColumn<UserDTO, String> tableColumnLastNameSelected;

    @FXML
    TextField textFieldComposeMessage;

    @FXML
    TableView<Message> tableViewInbox;

    @FXML
    TableColumn<Message, String> tableColumnInboxFirstName;

    @FXML
    TableColumn<Message, String> tableColumnInboxLastName;

    @FXML
    TableColumn<Message, String> tableColumnInboxDate;

    @FXML
    TableColumn<Message, String> tableColumnInboxMessage;

    @FXML
    TextField textFieldInboxMessage;

    @FXML
    public void initialize(){

        tableColumnFirstNameUnselected.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("firstName"));
        tableColumnLastNameUnselected.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("lastName"));

        tableColumnFirstNameSelected.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("firstName"));
        tableColumnLastNameSelected.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("lastName"));

        tableColumnInboxFirstName.setCellValueFactory(new PropertyValueFactory<Message, String>("FirstNameFrom"));
        tableColumnInboxLastName.setCellValueFactory(new PropertyValueFactory<Message, String>("LastNameFrom"));
        tableColumnInboxDate.setCellValueFactory(new PropertyValueFactory<Message, String>("DateString"));
        tableColumnInboxMessage.setCellValueFactory(new PropertyValueFactory<Message, String>("Message"));

        tableViewUnselected.setItems(modelUnselected);
        tableViewSelected.setItems(modelSelected);
        tableViewInbox.setItems(modelInbox);
    }

    public void setUtilizatorService(UtilizatorService utilizatorService) {
        this.utilizatorService = utilizatorService;
        initModel();
    }

    public void setMessageService(MessageService messageService) {

        this.messageService = messageService;
        initModelInbox();
    }

    public void setPrietenieService(PrietenieService prietenieService) {
        this.prietenieService = prietenieService;
    }

    public void setSelectedUserDTO(UserDTO selectedUserDTO) {

        this.selectedUserDTO = selectedUserDTO;
        if(selectedUserDTO != null) {
            labelUserCompose.setText("Logged in with: " + selectedUserDTO.getFirstName() + " " + selectedUserDTO.getLastName());
            labelUserInbox.setText("Logged in with: " + selectedUserDTO.getFirstName() + " " + selectedUserDTO.getLastName());
        }
    }

    private void initModel(){
        Iterable<Prietenie> prietenii = this.prietenieService.getAllFriendshipsUser(selectedUserDTO.getId());
        Long idSelectedUserDTO = selectedUserDTO.getId();

        List<UserDTO> listFriends = new ArrayList<>();
        prietenii.forEach(fr->{
            if(fr.getId().getRight().equals(idSelectedUserDTO)) {
                listFriends.add(utilizatorService.getUserDTO(fr.getId().getLeft()));
            }
            else{
                listFriends.add(utilizatorService.getUserDTO(fr.getId().getRight()));
            }
        });

        if(!prietenii.iterator().hasNext()){
            modelUnselected.setAll(listFriends);
            tableViewUnselected.setPlaceholder(new Label("You have no added friends"));
        }else{
            modelUnselected.setAll(listFriends);
            refreshTables(listFriends);

        }
    }

    private void initModelInbox(){
       Iterable<Message> allMessages = messageService.getMessageToUser(selectedUserDTO.getId());
       List<Message> listMessages = new ArrayList<>();
       allMessages.forEach(listMessages::add);
       if(!listMessages.iterator().hasNext()){
           modelInbox.setAll(listMessages);
           tableViewInbox.setPlaceholder(new Label("You have no messages"));
       }else{
           modelInbox.setAll(listMessages);
       }
    }

    public void refreshTables(List<UserDTO> listFriends){
        listUsersUnselected.clear();
        listUsersSelected.clear();

        listUsersUnselected.addAll(listFriends);
        modelSelected.setAll(listUsersSelected);
    }

    /**
     * muta din stanga in dreapta userul selectat
     */
    public void selectedUserForMessage() {
        UserDTO userDTO = tableViewUnselected.getSelectionModel().getSelectedItem();
        if(userDTO != null){
            listUsersSelected.add(userDTO);
            modelSelected.setAll(listUsersSelected);
            tableViewSelected.setItems(modelSelected);

            listUsersUnselected.remove(userDTO);
            modelUnselected.setAll(listUsersUnselected);
            tableViewUnselected.setItems(modelUnselected);
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "Notihg selected");
            alert.show();
            tableViewSelected.getSelectionModel().clearSelection();
        }
    }

    /**
     * muta din dreapta in stanga
     */
    public void unselectedUserForMessage() {
        UserDTO userDTO = tableViewSelected.getSelectionModel().getSelectedItem();
        if(userDTO != null){
            listUsersUnselected.add(userDTO);
            modelUnselected.setAll(listUsersUnselected);
            tableViewUnselected.setItems(modelUnselected);

            listUsersSelected.remove(userDTO);
            modelSelected.setAll(listUsersSelected);
            tableViewSelected.setItems(modelSelected);
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "Notihg selected");
            alert.show();
            tableViewUnselected.getSelectionModel().clearSelection();
        }
    }

    public void sendMessages() {
        String textMessage = textFieldComposeMessage.getText();


        if(!textMessage.matches("[ ]*")){
            if(listUsersSelected.size()!=0){
                List<Utilizator> listoTo = new ArrayList<>();
                listUsersSelected.forEach(user->{
                    listoTo.add(utilizatorService.findOne(user.getId()));
                });

                Message message = new Message(utilizatorService.findOne(selectedUserDTO.getId()), listoTo,
                        textMessage, LocalDateTime.now());
                messageService.addMessage(message);
                textFieldComposeMessage.clear();
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Successful send");
                alert.show();
                initModel();
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please select users");
                alert.show();
            }

        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please insert a text message");
            alert.show();
        }
    }

    public void replyMessage() {

        String textMessageReply = textFieldInboxMessage.getText();
        Message messageSelected = tableViewInbox.getSelectionModel().getSelectedItem();

        if(messageSelected != null){
            if(!textMessageReply.matches("[ ]*")){
                Utilizator user = utilizatorService.findOne(selectedUserDTO.getId());
                Message response = new Message(user, Arrays.asList( messageSelected.getFrom()),textMessageReply, LocalDateTime.now());
                messageService.addMessage(response);
                textFieldInboxMessage.clear();
                tableViewInbox.getSelectionModel().clearSelection();
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Successful send");
                alert.show();

            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please insert a text message");
                alert.show();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "Nothing selected");
            alert.show();
        }


    }

    public void exitFromInbox() {
        System.exit(0);
    }

    public void exitFromCompose() {
        System.exit(0);

    }



    Stage accountUserStage;
    Stage messageStage;

    public void setStage(Stage messageStage) {
        this.messageStage = messageStage;

    }

    public void setStageBack(Stage accountUserStage) {

        this.accountUserStage = accountUserStage;
    }

    public void backFromCompose() {
        accountUserStage.show();
        messageStage.hide();
    }

    public void backFromInbox( ){
        accountUserStage.show();
        messageStage.hide();
    }
}
