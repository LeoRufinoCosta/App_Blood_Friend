package br.com.tcc.blood_friend.Model;

public class User {
    String ID, Nome, Localizacao, TipoSanguineo, Status;

    public User(){}

    public User(String id, String nome, String localizacao, String tipoSanguineo, String status) {
        ID = id;
        Nome = nome;
        Localizacao = localizacao;
        TipoSanguineo = tipoSanguineo;
        Status = status;
    }
    public String getID(){ return ID; }

    public void setID(String id) { ID = id; }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getLocalizacao() {
        return Localizacao;
    }

    public void setLocalizacao(String localizacao) {
        Localizacao = localizacao;
    }

    public String getTipoSanguineo() {
        return TipoSanguineo;
    }

    public void setTipoSanguineo(String tipoSanguineo) {
        TipoSanguineo = tipoSanguineo;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
