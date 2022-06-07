package br.com.tcc.blood_friend.Model;

public class ChatUserModel {
    public String id;

    public ChatUserModel(String id) {
        this.id = id;
    }

    public ChatUserModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
