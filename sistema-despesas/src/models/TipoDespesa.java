package models;

public class TipoDespesa {
    private int id;
    private String nome;
    private String descricao;

    private static int proximoId = 1;

    public TipoDespesa(int id, String nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        if (id >= proximoId) {
            proximoId = id + 1;
        }
    }
    public TipoDespesa(String nome, String descricao) {
        this.id = proximoId++;
        this.nome = nome;
        this.descricao = descricao;
    }
    public TipoDespesa(String nome) {
        this(nome, "");
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public static int getProximoId() {
        return proximoId;
    }
    public static void resetarContador() {
        proximoId = 1;
    }
    public String toFileString() {
        return id + "|" + nome + "|" + descricao;
    }
    public static TipoDespesa fromFileString(String linha) {
        String[] partes = linha.split("\\|");
        if (partes.length >= 2) {
            int id = Integer.parseInt(partes[0]);
            String nome = partes[1];
            String descricao = partes.length > 2 ? partes[2] : "";
            return new TipoDespesa(id, nome, descricao);
        }
        return null;
    }
    
    @Override
    public String toString() {
        return "TipoDespesa{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                '}';
    }
}
