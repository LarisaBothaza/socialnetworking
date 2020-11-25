package socialnetwork.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import javafx.scene.Scene;
import javafx.stage.Stage;
import socialnetwork.config.ApplicationContext;
import socialnetwork.controller.IntroductionController;
import socialnetwork.domain.Prietenie;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.Utilizator;
import socialnetwork.domain.messages.FriendshipRequest;
import socialnetwork.domain.messages.Message;
import socialnetwork.domain.messages.ReplyMessage;
import socialnetwork.domain.validators.*;
import socialnetwork.repository.Repository;
import socialnetwork.repository.file.*;
import socialnetwork.service.*;

import java.io.IOException;

public class MainFX extends Application {
    private static PrietenieService prietenieService;
    private static UtilizatorService utilizatorService ;
    private static MessageService messageService;
    private static ReplyMessageService replyMessageService ;
    private static FriendshipRequestService friendshipRequestService;


    @Override
    public void start(Stage primaryStage) throws Exception {
//        Group root = new Group();
//        Scene scene = new Scene(root,500,500, Color.PINK);
//        primaryStage.setTitle("Test");
//        primaryStage.setScene(scene);
        initView(primaryStage);
        //prefHeight="400.0" prefWidth="250.0"
        primaryStage.setWidth(290);
        primaryStage.setHeight(450);

        primaryStage.setTitle("Welcome!");
        primaryStage.show();


    }

    public static void main(String[] args) {
        //configurations
        String fileNameUsers= ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.users");
        String fileNamePrietenii=ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.prietenii");
        String fileNameMessage=ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.mesaje");
        String fileNameConversation=ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.conversatii");
        String fileNameRequests=ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.cereri");
        //String fileName="data/users.csv";

        //repositories
        Repository<Long, Utilizator> userFileRepository = new UtilizatorFile(fileNameUsers, new UtilizatorValidator());
        Repository<Tuple<Long,Long>, Prietenie> prietenieFileRepository = new PrietenieFile(fileNamePrietenii,
                new PrietenieValidator(userFileRepository),userFileRepository);
        Repository<Long, Message> messageRepository = new MessageFile(fileNameMessage,new MessageValidator(),userFileRepository);
        Repository<Long, ReplyMessage> replyMessageRepository = new ReplyMessageFile(fileNameConversation,
                new ReplyMessageValidator(),userFileRepository);
        Repository<Long, FriendshipRequest> friendshipRequestRepository = new FriendshipRequestFile(fileNameRequests,
                new FriendshipRequestValidator(),userFileRepository);

        //services
        prietenieService = new PrietenieService(prietenieFileRepository, userFileRepository);
        utilizatorService = new UtilizatorService(userFileRepository,prietenieFileRepository);
        messageService = new MessageService(messageRepository);
        replyMessageService = new ReplyMessageService(replyMessageRepository);
        friendshipRequestService = new FriendshipRequestService(friendshipRequestRepository,prietenieFileRepository);

        launch(args);
    }

    private void initView(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/introduction.fxml"));
        AnchorPane layout = loader.load();
        primaryStage.setScene(new Scene((layout)));

        IntroductionController introductionController = loader.getController();
        introductionController.setUtilizatorService(utilizatorService);
        introductionController.setPrietenieService(prietenieService);
        introductionController.setFriendshipRequestService(friendshipRequestService);

    }
}

