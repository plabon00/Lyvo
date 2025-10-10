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


@Controller
@CrossOrigin("http://localhost:5173")
public class ChatController {

    @Autowired
    RoomRepo roomrepository;

    @MessageMapping("/sendMessage/{roomId}")
    @SendTo("/topic/room/{roomId}")
    public Message sendMessage(
            @DestinationVariable String roomId,
            MessageRequest request
    ){
        System.out.println("📨 Received . from -" + request.getSender());

        Room room = roomrepository.findByRoomId(roomId);

        // Create Message with auto timestamp
        Message message = new Message(request.getSender(), request.getContent());

        if (room != null) {
            room.getMassages().add(message);
            roomrepository.save(room);
        } else {
            throw new RuntimeException("Room not found: " + roomId);
        }

        System.out.println("✅ Message created with timestamp: " + message.getTimeStamp());
        return message; // This includes the auto-generated timestamp
    }

}
