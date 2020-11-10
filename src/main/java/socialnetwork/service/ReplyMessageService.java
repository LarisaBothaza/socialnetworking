package socialnetwork.service;

import socialnetwork.domain.messages.Message;
import socialnetwork.domain.messages.ReplyMessage;
import socialnetwork.repository.Repository;

import java.util.ArrayList;
import java.util.List;

public class ReplyMessageService {
    private Repository<Long, ReplyMessage> replyMessageRepository;

    public ReplyMessageService(Repository<Long, ReplyMessage> replyMessageRepository) {
        this.replyMessageRepository = replyMessageRepository;
    }

    /**
     * add a reply message
     * @param replyMessageParam
     * @return
     */
    public ReplyMessage addMessage(ReplyMessage replyMessageParam){
        ReplyMessage replyMessage = replyMessageRepository.save(replyMessageParam);
        //mesage validator sevice pt a verifica daca exista userii
        return replyMessage;
    }

    /**
     * search for a reply message by id
     * @param idReplyMessage Long
     * @return
     */
    public ReplyMessage getReplyMessage(Long idReplyMessage){
        return replyMessageRepository.findOne(idReplyMessage);
    }

    /**
     *returns the conversation between 2 users
     * @param idSender Long
     * @param idReceiver Long
     * @return
     */
    public Iterable<ReplyMessage> getConversation(Long idSender, Long idReceiver){
        Iterable<ReplyMessage> listReplyMessages = replyMessageRepository.findAll();
        List<ReplyMessage> conversation = new ArrayList<>();
        listReplyMessages.forEach(x->{
            if(x.getFrom().getId().equals(idSender) && x.getTo().get(0).getId().equals(idReceiver)||
                    x.getFrom().getId().equals(idReceiver) && x.getTo().get(0).getId().equals(idSender)){
                conversation.add(x);
            }
        });
        return conversation;
    }

}
