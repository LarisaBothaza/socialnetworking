package socialnetwork;

import socialnetwork.config.ApplicationContext;
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
import socialnetwork.ui.UI;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String fileNameUsers=ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.users");
        String fileNamePrietenii=ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.prietenii");
        String fileNameMessage=ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.mesaje");
        String fileNameConversation=ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.conversatii");
        String fileNameRequests=ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.cereri");
        //String fileName="data/users.csv";

        Repository<Long,Utilizator> userFileRepository = new UtilizatorFile(fileNameUsers, new UtilizatorValidator());
        Repository<Tuple<Long,Long>, Prietenie> prietenieFileRepository = new PrietenieFile(fileNamePrietenii,
                new PrietenieValidator(userFileRepository),userFileRepository);
        Repository<Long, Message> messageRepository = new MessageFile(fileNameMessage,new MessageValidator());
        Repository<Long, ReplyMessage> replyMessageRepository = new ReplyMessageFile(fileNameConversation,
                new ReplyMessageValidator(),userFileRepository);
        Repository<Long, FriendshipRequest> friendshipRequestRepository = new FriendshipRequestFile(fileNameRequests,
                new FriendshipRequestValidator(),userFileRepository);


        PrietenieService prietenieService = new PrietenieService(prietenieFileRepository, userFileRepository);
        UtilizatorService utilizatorService = new UtilizatorService(userFileRepository,prietenieFileRepository);
        MessageService messageService = new MessageService(messageRepository);
        ReplyMessageService replyMessageService = new ReplyMessageService(replyMessageRepository);
        FriendshipRequestService friendshipRequestService = new FriendshipRequestService(friendshipRequestRepository);

        UI ui = new UI(utilizatorService, prietenieService, messageService, replyMessageService, friendshipRequestService);
        ui.run();

        //userFileRepository.findAll().forEach(System.out::println);
        //prietenieFileRepository.findAll().forEach(System.out::println);
        //replyMessageRepository.findAll().forEach(System.out::println);
        //friendshipRequestRepository.findAll().forEach(System.out::println);
    }
}


