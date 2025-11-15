package models;


public class Usuario {
    private int id;
    private String login;
    private String senhaCriptografada;
    private String nome;
    private boolean ativo;

    private static int proximoId = 1;
    public Usuario(int id, String login, String senhaCriptografada, String nome, boolean ativo) {
        this.id = id;
        this.login = login;
        this.senhaCriptografada = senhaCriptografada;
        this.nome = nome;
        this.ativo = ativo;
        if (id >= proximoId) {
            proximoId = id + 1;
        }
    }
    public Usuario(String login, String senhaCriptografada, String nome) {
        this.id = proximoId++;
        this.login = login;
        this.senhaCriptografada = senhaCriptografada;
        this.nome = nome;
        this.ativo = true;
    }

    public Usuario(String login, String senhaCriptografada) {
        this(login, senhaCriptografada, login);
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public String getSenhaCriptografada() {
        return senhaCriptografada;
    }
    public void setSenhaCriptografada(String senhaCriptografada) {
        this.senhaCriptografada = senhaCriptografada;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public boolean isAtivo() {
        return ativo;
    }
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
    public static void resetarContador() {
        proximoId = 1;
    }
    public String toFileString() {
        return id + "|" + login + "|" + senhaCriptografada + "|" + nome + "|" + ativo;
    }
    public static Usuario fromFileString(String linha) {
        String[] partes = linha.split("\\|");
        if (partes.length >= 4) {
            int id = Integer.parseInt(partes[0]);
            String login = partes[1];
            String senha = partes[2];
            String nome = partes[3];
            boolean ativo = partes.length > 4 ? Boolean.parseBoolean(partes[4]) : true;
            return new Usuario(id, login, senha, nome, ativo);
        }
        return null;
    }
    
    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", nome='" + nome + '\'' +
                ", ativo=" + ativo +
                '}';
    }
}
