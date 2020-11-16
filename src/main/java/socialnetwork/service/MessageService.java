package socialnetwork.service;

import socialnetwork.domain.Utilizator;
import socialnetwork.domain.messages.Message;
import socialnetwork.repository.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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
    /**
     * search for a message by id
     * @param  idMessage Long
     * @return
     */
    public Message getMessage(Long idMessage){
        return messageRepository.findOne(idMessage);
    }

    public Iterable<Message> getAll(){
        return messageRepository.findAll();
    }

    public Iterable<Message> getMessageToUser (Long id){
        Iterable<Message> listIterableAllMessage = messageRepository.findAll();

       List<Message> filterList = new ArrayList<>();

//        10;3;7,8,9;Noiembrie;2020-11-11 11:17:14
//        11;9;4,6;Salut!;2020-11-11 14:49:53

        listIterableAllMessage.forEach(message->{
            List<Utilizator> listUsersTo = message.getTo();

            AtomicBoolean b = new AtomicBoolean(false);
            listUsersTo.forEach(user->{
                if(user.getId().equals(id)){
                    b.set(true);
                }
            });
            if(b.get()){
                filterList.add(message);

            }
        });
        return filterList;
    }

}
