package domain;

import java.util.Date;

public class Message {
    private String message;
    private String sender;
    private int groupID;
    private int id;
    private Date timeSent;


    /**
     * Constructs a Message object. Used to store Messages pulled from the database.
     * @param id - id of the message in the database
     * @param groupID - id of the group the message belongs to
     * @param sender - netID of the sender of the message
     * @param message - the message
     * @param timeSent - the time that the message was sent
     */
    public Message(int id, int groupID, String sender, String message, Date timeSent){
        this.id = id;
        this.message = message;
        this.sender = sender;
        this.groupID = groupID;
        this.timeSent = timeSent;
    }

    /**
     * Constructs a Message object. Used to Store a message before sending it to the datbase
     * @param groupID - id of the group the message belongs to
     * @param sender - netID of the sender of the message
     * @param message - the message
     */
    public Message(int groupID, String sender, String message){
        this.groupID = groupID;
        this.sender = sender;
        this.message = message;
    }

    /**
     * Get the message from the Message object
     * @return message
     */
    public String getMessage(){
        return message;
      }

    /**
     * Get the sender of the message
     * @return sender
     */
    public String getSender(){
    return sender;
  }

    /**
     * Get the id of the group the message belongs to
     * @return groupID
     */
    public int getGroupID(){
    return groupID;
  }

    /**
     * Get the time that the message was sent
     * @return timeSent
     */
    public Date getTimeSent(){
    return timeSent;
  }

    /**
     * Get the id of the message in the database (only use on messages pulled from the database)
     * @return id
     */
    public int getID(){
    return id;
  }

    /**
     * Get the string representation of the Message object
     * @return message in string form
     */
    public String toString(){
    return sender + " (" + timeSent + "): " + message;
  }
}
