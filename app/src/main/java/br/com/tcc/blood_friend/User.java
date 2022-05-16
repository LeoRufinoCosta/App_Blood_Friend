package br.com.tcc.blood_friend;

public class User {
    String Nome, Localizacao, TipoSanguineo;

    public User(){}

    public User(String nome, String localizacao, String tipoSanguineo) {
        Nome = nome;
        Localizacao = localizacao;
        TipoSanguineo = tipoSanguineo;
    }

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
}
