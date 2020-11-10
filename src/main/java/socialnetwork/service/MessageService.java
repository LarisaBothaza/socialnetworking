package socialnetwork.service;

import socialnetwork.domain.messages.Message;
import socialnetwork.repository.Repository;

public class MessageService {
    private Repository<Long, Message> messageRepository;

    public MessageService(Repository<Long, Message> messageRepository) {
        this.messageRepository = messageRepository;
    }

    /**
     * add a message
     * @param messageParam
     * @return
     */
    public Message addMessage(Message messageParam){
        Message message = messageRepository.save(messageParam);
        //mesage validator sevice pt a verifica daca exista userii
        return message;
    }
}
