package socialnetwork.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import socialnetwork.domain.Prietenie;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.UserDTO;
import socialnetwork.domain.Utilizator;
import socialnetwork.service.FriendshipRequestService;
import socialnetwork.service.MessageService;
import socialnetwork.service.PrietenieService;
import socialnetwork.service.UtilizatorService;
import socialnetwork.utils.events.FriendshipChangeEvent;
import socialnetwork.utils.observer.Observable;
import socialnetwork.utils.observer.Observer;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AccountUserController implements Observer<FriendshipChangeEvent> {
    PrietenieService prietenieService;
    UserDTO selectedUserDTO;
    MessageService messageService;
    UtilizatorService utilizatorService;
    FriendshipRequestService friendshipRequestService;
    ObservableList<UserDTO> model = FXCollections.observableArrayList();

    @FXML
    TableView<UserDTO> tableViewAccountUser;

    @FXML
    TableColumn<UserDTO, String> tabelColumnFirstName;

    @FXML
    TableColumn<UserDTO, String> tabelColumnLastName;

    @FXML Label labelUser;

    Stage accountUserStage;

    @FXML
    public void initialize(){
        tabelColumnFirstName.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("firstName"));
        tabelColumnLastName.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("lastName"));
        tableViewAccountUser.setItems(model);
    }

    /**
     * setting the attributes
     * @param prietenieService
     * @param utilizatorService
     * @param friendshipRequestService
     * @param selectedUserDTO
     * @param accountUserStage
     */
    void setAttributes(PrietenieService prietenieService, UtilizatorService utilizatorService,
                       FriendshipRequestService friendshipRequestService,MessageService messageService, UserDTO selectedUserDTO, Stage accountUserStage){
        this.prietenieService = prietenieService;
        this.utilizatorService = utilizatorService;
        this.friendshipRequestService = friendshipRequestService;
        this.messageService = messageService;
        this.prietenieService.addObserver(this);
        this.selectedUserDTO = selectedUserDTO;
        this.accountUserStage = accountUserStage;
        if(selectedUserDTO != null){
            labelUser.setText("Hello, " + selectedUserDTO.getFirstName()+" "+selectedUserDTO.getLastName());
           initModel();

        }
    }

    public void deleteFriendship() {
        UserDTO userDTO = tableViewAccountUser.getSelectionModel().getSelectedItem();
        if(userDTO != null){
            Long idSelectedUser = selectedUserDTO.getId();
            Long idUserDTO = userDTO.getId();
            Prietenie prietenie1 = prietenieService.getOne(idSelectedUser,idUserDTO);
            Prietenie prietenie2 = prietenieService.getOne(idUserDTO,idSelectedUser);
            if(prietenie1 != null){
                prietenieService.deletePrietenie(new Tuple<>(idSelectedUser,idUserDTO));
            }

            if(prietenie2!= null){
                prietenieService.deletePrietenie(new Tuple<>(idUserDTO,idSelectedUser));
            }

            tableViewAccountUser.getSelectionModel().clearSelection();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "Nothing selected");
            alert.show();
        }

    }
    public void addFriendshipRequest() throws IOException {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/addFriendshipView.fxml"));
            AnchorPane root = loader.load();

            Stage addFriendshipRequestStage = new Stage();
            addFriendshipRequestStage.setTitle("Send friendship request");
            addFriendshipRequestStage.setResizable(false);
            addFriendshipRequestStage.initModality(Modality.APPLICATION_MODAL);
            addFriendshipRequestStage.setOnCloseRequest(event -> {
                accountUserStage.show();
                tableViewAccountUser.getSelectionModel().clearSelection();
            } );

            Scene scene = new Scene(root);
            addFriendshipRequestStage.setScene(scene);

            AddFriendshipViewController addFriendshipViewController = loader.getController();
            addFriendshipViewController.setPrietenieService(prietenieService);
            addFriendshipViewController.setUtilizatorService(utilizatorService,selectedUserDTO);
            addFriendshipViewController.setFriendshipRequestService(friendshipRequestService);

            addFriendshipViewController.setStage(addFriendshipRequestStage);
            addFriendshipViewController.setStageBack(accountUserStage);

            accountUserStage.hide();
            addFriendshipRequestStage.show();

        }catch (IOException e){
            e.printStackTrace();
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
            model.setAll(listFriends);
            tableViewAccountUser.setPlaceholder(new Label("You have no added friends"));
        }else{
            model.setAll(listFriends);
        }
    }

    @Override
    public void update(FriendshipChangeEvent friendshipChangeEvent) {
            initModel();
    }

    public void viewFriendshipRequests(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/friendshipRequestsView.fxml"));
            AnchorPane root = loader.load();

            Stage friendshipRequestViewStage = new Stage();
            friendshipRequestViewStage.setScene(new Scene(root));
            friendshipRequestViewStage.setTitle("Friendship Requests");
            friendshipRequestViewStage.setOnCloseRequest(event -> {
                accountUserStage.show();
                tableViewAccountUser.getSelectionModel().clearSelection();
            } );

            FriendshipRequestsViewController friendshipRequestsViewController = loader.getController();
            friendshipRequestsViewController.setFriendshipRequestService(friendshipRequestService,selectedUserDTO);
            friendshipRequestsViewController.setPrietenieService(prietenieService);

            friendshipRequestsViewController.setStageBack(accountUserStage);
            friendshipRequestsViewController.setStage(friendshipRequestViewStage);

            accountUserStage.hide();
            friendshipRequestViewStage.show();

        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public void openMessages() {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/messageView.fxml"));

            AnchorPane root = loader.load();

            Stage messagesViewStage = new Stage();
            messagesViewStage.setScene(new Scene(root));
            messagesViewStage.setTitle("Messages");
            messagesViewStage.setOnCloseRequest(event -> {
                accountUserStage.show();
                tableViewAccountUser.getSelectionModel().clearSelection();
            } );

            MessageController messageController = loader.getController();

            messageController.setPrietenieService(prietenieService);
            messageController.setSelectedUserDTO(selectedUserDTO);
            messageController.setUtilizatorService(utilizatorService);
            messageController.setMessageService(messageService);
            messageController.setStageBack(accountUserStage);
            messageController.setStage(messagesViewStage);

            accountUserStage.hide();
            messagesViewStage.show();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void exitFromAccountUser() {
        System.exit(0);
    }
}
