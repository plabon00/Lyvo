package com.apex.lyvo.repositories;


import com.apex.lyvo.Entities.Room;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepo extends MongoRepository<Room , String> {


    Room findByRoomId(String roomId);



}
