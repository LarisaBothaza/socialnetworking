package socialnetwork.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import socialnetwork.domain.Prietenie;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.UserDTO;
import socialnetwork.domain.Utilizator;
import socialnetwork.domain.messages.FriendshipRequest;
import socialnetwork.service.FriendshipRequestService;
import socialnetwork.service.PrietenieService;
import socialnetwork.utils.events.FriendshipRequestChangeEvent;
import socialnetwork.utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FriendshipRequestsViewController implements Observer<FriendshipRequestChangeEvent> {
    FriendshipRequestService friendshipRequestService;
    PrietenieService prietenieService;
    UserDTO selectedUserDTO;
    ObservableList<FriendshipRequest> model = FXCollections.observableArrayList();
    @FXML
    Label labelUser;

    @FXML
    TableView<FriendshipRequest> tableViewFriendshipRequests;

    @FXML
    TableColumn<FriendshipRequest, String> tableColumnFirstName;

    @FXML
    TableColumn<FriendshipRequest, String> tableColumnLastName;

    @FXML
    TableColumn<FriendshipRequest, String> tableColumnMessage;

    @FXML
    TableColumn<FriendshipRequest, String> tableColumnDate;

    @FXML
    TableColumn<FriendshipRequest, String> tableColumnStatus;

    @FXML
    Button buttonAccept;

    @FXML
    Button buttonReject;

    @FXML
    Button buttonUnsend;

    @FXML
    Button buttonFrom;

    @FXML
    Button buttonTo;

    @FXML
    RadioButton radioButtonPending;
    @FXML
    RadioButton radioButtonApproved;
    @FXML
    RadioButton radioButtonRejected;

    @FXML
    TableColumn<FriendshipRequest, String> tableColumnFromTo;

    public void setFriendshipRequestService(FriendshipRequestService friendshipRequestService, UserDTO selectedUserDTO) {
        this.friendshipRequestService = friendshipRequestService;
        this.selectedUserDTO = selectedUserDTO;
        this.friendshipRequestService.addObserver(this);
        if(selectedUserDTO != null) {
            labelUser.setText("Logged in with: " + selectedUserDTO.getFirstName() + " " + selectedUserDTO.getLastName());
        }
        initModel();
    }

    public void setPrietenieService(PrietenieService prietenieService) {
        this.prietenieService = prietenieService;
    }

    @FXML
    public void initialize() {
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstNameFrom"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<>("lastNameFrom"));
        tableColumnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        tableColumnMessage.setCellValueFactory(new PropertyValueFactory<>("message"));
        tableColumnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableViewFriendshipRequests.setItems(model);
    }

    public void initModel(){
       if(tableColumnFromTo.getText().equals("From")){
           List<FriendshipRequest> friendshipRequestList = friendshipRequestService.getAllRequest(selectedUserDTO.getId());
           if(friendshipRequestList.size() == 0){
               model.setAll(friendshipRequestList);
               tableViewFriendshipRequests.setPlaceholder(new Label("You have no friendship requests"));
           }else{
               model.setAll(friendshipRequestList);
           }
       }else
           if(tableColumnFromTo.getText().equals("To")){
               List<FriendshipRequest> friendshipRequestList = friendshipRequestService.getAllRequestTo(selectedUserDTO.getId());

               if(friendshipRequestList.size() == 0){
                   model.setAll(friendshipRequestList);
                   tableViewFriendshipRequests.setPlaceholder(new Label("You have no sent friendship requests"));
               }else{
                   model.setAll(friendshipRequestList);
               }
           }
    }

    public void acceptedRequest() {
        FriendshipRequest friendshipRequestSelected = tableViewFriendshipRequests.getSelectionModel().getSelectedItem();
        if(friendshipRequestSelected != null){
            if(friendshipRequestSelected.getStatus().equals("pending")){
                friendshipRequestService.updateFriendshipRequest(friendshipRequestSelected,"approved");
                Prietenie newFriendship = new Prietenie(new Tuple<>(selectedUserDTO.getId(), friendshipRequestSelected.getFrom().getId()));
                prietenieService.addPrietenie(newFriendship);
            }
        }
    }

    @Override
    public void update(FriendshipRequestChangeEvent friendshipRequestChangeEvent) {
        initModel();
    }

    public void rejectedRequest() {
        FriendshipRequest friendshipRequestSelected = tableViewFriendshipRequests.getSelectionModel().getSelectedItem();
        if(friendshipRequestSelected != null){
            if(friendshipRequestSelected.getStatus().equals("pending")){
                friendshipRequestService.updateFriendshipRequest(friendshipRequestSelected,"rejected");
            }
        }
    }

    public void handleFilterPending() {
        if(radioButtonPending.isSelected()){
            List<FriendshipRequest> friendshipRequestList = friendshipRequestService.getAllRequest(selectedUserDTO.getId());
            model.setAll(
                    friendshipRequestList.stream()
                            .filter(fr -> fr.getStatus().equals("pending"))
                            .collect(Collectors.toList())
            );
        }else{
            initModel();
        }
    }

    public void handleFilterApproved() {
        if(radioButtonApproved.isSelected()){
            List<FriendshipRequest> friendshipRequestList = friendshipRequestService.getAllRequest(selectedUserDTO.getId());
            model.setAll(
                    friendshipRequestList.stream()
                            .filter(fr -> fr.getStatus().equals("approved"))
                            .collect(Collectors.toList())
            );
        }else{
            initModel();
        }
    }

    public void handleFilterRejected() {
        if(radioButtonRejected.isSelected()){
            List<FriendshipRequest> friendshipRequestList = friendshipRequestService.getAllRequest(selectedUserDTO.getId());
            model.setAll(
                    friendshipRequestList.stream()
                            .filter(fr -> fr.getStatus().equals("rejected"))
                            .collect(Collectors.toList())
            );
        }else{
            initModel();
        }
    }

    public void handleHideFrom() {
        buttonUnsend.setVisible(false);
        buttonAccept.setVisible(true);
        buttonReject.setVisible(true);
        radioButtonPending.setVisible(true);
        radioButtonApproved.setVisible(true);
        radioButtonRejected.setVisible(true);
        tableColumnFromTo.setText("From");
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstNameFrom"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<>("lastNameFrom"));
        initModel();
    }

    public void handleHideTo() {
        buttonUnsend.setVisible(true);
        buttonAccept.setVisible(false);
        buttonReject.setVisible(false);
        radioButtonPending.setVisible(false);
        radioButtonApproved.setVisible(false);
        radioButtonRejected.setVisible(false);
        tableColumnFromTo.setText("To");
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstNameTo"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<>("lastNameTo"));
        initModel();
    }

    public void deleteFriendshipRequest() {
        FriendshipRequest friendshipRequest = tableViewFriendshipRequests.getSelectionModel().getSelectedItem();;
        if(friendshipRequest != null){
            friendshipRequestService.deleteRequest(friendshipRequest.getId());
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "Nothing selected");
            alert.show();
        }
    }

    Stage accountUserStage;
    Stage friendshipRequestViewStage;

    public void setStageBack(Stage accountUserStage) {
        this.accountUserStage = accountUserStage;
    }

    public void setStage(Stage friendshipRequestViewStage) {
        this.friendshipRequestViewStage = friendshipRequestViewStage;
    }

    public void exitFromFriendshipRequests() {
        System.exit(0);
    }

    public void backFromFriendshipRequests() {
        accountUserStage.show();
        friendshipRequestViewStage.hide();
    }
}
