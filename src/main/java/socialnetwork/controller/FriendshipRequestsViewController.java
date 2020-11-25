package socialnetwork.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
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



    public void initialize() {
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tableColumnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        tableColumnMessage.setCellValueFactory(new PropertyValueFactory<>("message"));
        tableColumnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableViewFriendshipRequests.setItems(model);
    }

    public void initModel(){
        List<FriendshipRequest> friendshipRequestList = friendshipRequestService.getAllRequest(selectedUserDTO.getId());
//        friendshipRequestList.forEach(System.out::println);
        if(friendshipRequestList.size() == 0){
            model.setAll(friendshipRequestList);
            tableViewFriendshipRequests.setPlaceholder(new Label("You have no friendship requests"));
        }else{
            model.setAll(friendshipRequestList);
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

    @FXML
    RadioButton radioButtonPending;
    @FXML
    RadioButton radioButtonApproved;
    @FXML
    RadioButton radioButtonRejected;

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
}
