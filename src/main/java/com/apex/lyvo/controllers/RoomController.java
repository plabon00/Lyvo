package com.apex.lyvo.controllers;


import com.apex.lyvo.Entities.Message;
import com.apex.lyvo.Entities.Room;
import com.apex.lyvo.repositories.RoomRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
@CrossOrigin("http://localhost:5173")
public class RoomController {

    @Autowired
    private RoomRepo roomrepository ;
    //create Room


    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody String roomId) {
        if (roomrepository.findByRoomId(roomId) != null) {
            return ResponseEntity.badRequest().body("Room Already Exists !!");
        }

        Room room = new Room();
        room.setRoomId(roomId);
        Room savedRoom = roomrepository.save(room);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedRoom);
    }

    //get Room

    @GetMapping("/{roomId}")
    public ResponseEntity<?> joinRoom(@PathVariable String roomId){
        Room room = roomrepository.findByRoomId(roomId) ;

        if (room == null){
            return ResponseEntity.badRequest().body("Room Doesn't Exists") ;
        }
        return ResponseEntity.ok().body(room) ;

    }

    //get room Messages

    @GetMapping("/{roomId}/messages")
    public ResponseEntity<List<Message>> getMassages(
            @PathVariable String roomId ,
            @RequestParam(value = "page" , defaultValue = "0" , required = false) int page ,
            @RequestParam(value = "size" , defaultValue = "20" , required = false) int size

    ){

        Room room = roomrepository.findByRoomId(roomId) ;

        if (room == null){
            return ResponseEntity.badRequest().build() ;
        }
        //else
        List<Message> messages = room.getMassages() ;

        int start = Math.max(0,messages.size() - (page+1) * size);
        int end = Math.min(messages.size() , start+size) ;

        List<Message> paginatedMessages = messages.subList(start , end) ;

        return ResponseEntity.ok(paginatedMessages) ;
    }

    @PostMapping("/{roomId}/messages")
    public ResponseEntity<List<Message>> postMassages(@PathVariable String roomId , @RequestBody Message message){
        Room room = roomrepository.findByRoomId(roomId) ;
        if (room == null){
            return ResponseEntity.badRequest().build();
        }

        if (message.getTimeStamp() == null){
            message.setTimeStamp(LocalDateTime.now());
        }

        room.getMassages().add(message) ;

        Room savedRoom = roomrepository.save(room) ;

        return ResponseEntity.accepted().build() ;

    }

}
