package com.apex.lyvo.controllers;


import com.apex.lyvo.Entities.Message;
import com.apex.lyvo.Entities.Room;
import com.apex.lyvo.playload.MessageRequest;
import com.apex.lyvo.repositories.RoomRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@CrossOrigin("https://localhost:3000")
public class ChatController {

    @Autowired
    RoomRepo roomrepository ;

    /* For Sending And Receiving Messages */
    @MessageMapping("/sendMessage/{roomId}")
    @SendTo("/topic/room/{roomId}")
    public Message sendMessage(
            @DestinationVariable String roomId,
            @RequestBody MessageRequest request
    ){

        Room room = roomrepository.findByRoomId(request.getRoomId()) ;

        Message message = new Message() ;

        message.setContent(request.getContent());
        message.setSender(request.getSender());
        message.setTimeStamp(request.getMessageTime());

        if (room != null){
            room.getMassages().add(message);
            roomrepository.save(room);
        }else {
            throw new RuntimeException("Room not found !!!");
        }

        return message ;

    }



}
