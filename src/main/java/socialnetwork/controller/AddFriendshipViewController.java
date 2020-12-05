package socialnetwork.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import socialnetwork.domain.Prietenie;
import socialnetwork.domain.UserDTO;
import socialnetwork.domain.Utilizator;
import socialnetwork.domain.messages.FriendshipRequest;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.service.FriendshipRequestService;
import socialnetwork.service.PrietenieService;
import socialnetwork.service.UtilizatorService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AddFriendshipViewController {
    ObservableList<UserDTO> model = FXCollections.observableArrayList();
    UtilizatorService utilizatorService;
    PrietenieService prietenieService;
    FriendshipRequestService friendshipRequestService;
    UserDTO selectedUserDTO;

    @FXML
    TableView<UserDTO> tableViewNonFriends;

    @FXML
    TableColumn<UserDTO, String> tableColumnFirstName;

    @FXML
    TableColumn<UserDTO, String> tableColumnLastName;

    @FXML
    TextField textFieldMessage;

    @FXML
    TextField textFieldSearch;

    @FXML
    Label labelUser;

    @FXML
    public void initialize(){
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName")); //camp din UserDTO
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tableViewNonFriends.setItems(model);
    }

    public void setUtilizatorService(UtilizatorService utilizatorService, UserDTO selectedUserDTO) {
        this.utilizatorService = utilizatorService;
        this.selectedUserDTO = selectedUserDTO;
        if(selectedUserDTO != null) {
            labelUser.setText("Logged in with: " + selectedUserDTO.getFirstName() + " " + selectedUserDTO.getLastName());
        }
        initModel();
    }

    public void setPrietenieService(PrietenieService prietenieService) {
        this.prietenieService = prietenieService;
    }

    public void setFriendshipRequestService(FriendshipRequestService friendshipRequestService) {
        this.friendshipRequestService = friendshipRequestService;
    }

    private void initModel(){
        Iterable<Utilizator> users = utilizatorService.getAll();

        List<UserDTO> allNonFriends = new ArrayList<>();

        users.forEach(user->{
            if(prietenieService.getOne(selectedUserDTO.getId(),user.getId()) == null
                    && prietenieService.getOne(user.getId(),selectedUserDTO.getId()) == null
                    && !user.getId().equals(selectedUserDTO.getId())){
                UserDTO userDTO = new UserDTO(user.getFirstName(),user.getLastName());
                userDTO.setId(user.getId());
                allNonFriends.add(userDTO);
            }
        });

        if(allNonFriends.size() == 0){
            model.setAll(allNonFriends);
            tableViewNonFriends.setPlaceholder(new Label("You are a friend of all users!"));
        }else{
            model.setAll(allNonFriends);
        }

    }

    public void sendFriendshipRequest(){
        UserDTO userToDTO = tableViewNonFriends.getSelectionModel().getSelectedItem();
        if (userToDTO != null) {
            String message = textFieldMessage.getText();

            Utilizator userFrom = utilizatorService.findOne(selectedUserDTO.getId());
            Utilizator userTo = utilizatorService.findOne(userToDTO.getId());

            if(message.matches("[ ]*")){
                message = userFrom.getFirstName() + " " + userFrom.getLastName() + " has send you a friendship request";
            }

            FriendshipRequest friendshipRequest = new FriendshipRequest(userFrom, Arrays.asList(userTo),message,
                    LocalDateTime.now(),"pending");

            try{
                friendshipRequestService.addRequest(friendshipRequest);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Friendship request sended!");
                alert.show();
            }catch (ValidationException validationException){
                Alert alert = new Alert(Alert.AlertType.ERROR, "You have already sent a friendship request!");
                alert.show();
            }

            tableViewNonFriends.getSelectionModel().clearSelection();

        }

    }

    public void handleFilter() {
        Iterable<Utilizator> users = utilizatorService.getAll();

        List<UserDTO> allNonFriends = new ArrayList<>();

        users.forEach(user->{
            if(prietenieService.getOne(selectedUserDTO.getId(),user.getId()) == null
                    && prietenieService.getOne(user.getId(),selectedUserDTO.getId()) == null
                    && !user.getId().equals(selectedUserDTO.getId())){
                UserDTO userDTO = new UserDTO(user.getFirstName(),user.getLastName());
                userDTO.setId(user.getId());
                allNonFriends.add(userDTO);
            }
        });

        model.setAll(
                allNonFriends.stream().filter(user->user.getFirstName().startsWith(textFieldSearch.getText()) ||
                        user.getLastName().startsWith(textFieldSearch.getText()))
                        .collect(Collectors.toList()));

    }
}
