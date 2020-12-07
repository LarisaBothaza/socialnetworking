package socialnetwork.controller;

import com.sun.glass.ui.Application;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import socialnetwork.domain.Prietenie;
import socialnetwork.domain.UserDTO;
import socialnetwork.service.FriendshipRequestService;
import socialnetwork.service.MessageService;
import socialnetwork.service.PrietenieService;
import socialnetwork.service.UtilizatorService;

import java.io.IOException;

public class IntroductionController {
    UtilizatorService utilizatorService;
    PrietenieService prietenieService;
    FriendshipRequestService friendshipRequestService;
    MessageService messageService;
    ObservableList<UserDTO> modelUserDTO = FXCollections.observableArrayList();

    @FXML
    TableView<UserDTO> tableViewUserDTO;

    @FXML
    TableColumn<UserDTO, String> tableColumnFirstName;

    @FXML
    TableColumn<UserDTO, String> tableColumnLastName;

    Stage introductionstage;

    @FXML
    public void initialize(){
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("lastName"));
        tableViewUserDTO.setItems(modelUserDTO);
    }

    public void setIntroductionstage(Stage introductionstage) {
        this.introductionstage = introductionstage;
    }

    public void setUtilizatorService(UtilizatorService utilizatorService) {
        this.utilizatorService = utilizatorService;
        modelUserDTO.setAll(this.utilizatorService.getAllUsersDTO());
    }

    public void setPrietenieService(PrietenieService prietenieService) {

        this.prietenieService = prietenieService;
    }

    public void setFriendshipRequestService(FriendshipRequestService friendshipRequestService) {
        this.friendshipRequestService = friendshipRequestService;
    }

    public void selectFriendsUser(){
        UserDTO selectedUserDTO = tableViewUserDTO.getSelectionModel().getSelectedItem();
        //prietenieService.getAllFriendshipsUser(selectedUserDTO.getId()).forEach(System.out::println);
        if(selectedUserDTO != null){
            showAccountUser(selectedUserDTO);

        }

    }

    private void showAccountUser(UserDTO selectedUserDTO){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/accountUser.fxml"));
            AnchorPane root = loader.load();

            Stage accountUserStage = new Stage();
            accountUserStage.setTitle("User account");
            accountUserStage.initModality(Modality.APPLICATION_MODAL);
            accountUserStage.setOnCloseRequest(event -> {
                introductionstage.show();
            } );

            Scene scene = new Scene(root);
            accountUserStage.setScene(scene);
            AccountUserController accountUserController = loader.getController();

            accountUserController.setAttributes(prietenieService,utilizatorService,friendshipRequestService,messageService,selectedUserDTO,accountUserStage);
            accountUserController.setIntroductionStage(introductionstage);
            introductionstage.hide();
            tableViewUserDTO.getSelectionModel().clearSelection();
            accountUserStage.show();


        }
        catch (IOException e){
            e.printStackTrace();
        }
    }


    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }
}
