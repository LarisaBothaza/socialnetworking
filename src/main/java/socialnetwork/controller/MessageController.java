package socialnetwork.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
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
import java.util.List;

public class MessageController {
    ObservableList<UserDTO> modelUnselected = FXCollections.observableArrayList();
    ObservableList<UserDTO> modelSelected = FXCollections.observableArrayList();

    UtilizatorService utilizatorService;
    MessageService messageService;
    PrietenieService prietenieService;
    UserDTO selectedUserDTO;

    List<UserDTO> listUsersSelected= new ArrayList<>();
    List<UserDTO> listUsersUnselected = new ArrayList<>();

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
    public void initialize(){

        tableColumnFirstNameUnselected.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("firstName"));
        tableColumnLastNameUnselected.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("lastName"));

        tableColumnFirstNameSelected.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("firstName"));
        tableColumnLastNameSelected.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("lastName"));

        tableViewUnselected.setItems(modelUnselected);
        tableViewSelected.setItems(modelSelected);
    }

    public void setUtilizatorService(UtilizatorService utilizatorService) {
        this.utilizatorService = utilizatorService;
        initModel();
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    public void setPrietenieService(PrietenieService prietenieService) {
        this.prietenieService = prietenieService;
    }

    public void setSelectedUserDTO(UserDTO selectedUserDTO) {
        this.selectedUserDTO = selectedUserDTO;
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
}
