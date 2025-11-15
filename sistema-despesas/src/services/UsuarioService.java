package services;

import java.util.ArrayList;
import java.util.List;
import models.Usuario;
import utils.CriptografiaUtil;
import utils.FileManager;

public class UsuarioService {
    private static final String ARQUIVO = "usuarios.txt";
    private final List<Usuario> usuarios;
    
    public UsuarioService() {
        this.usuarios = new ArrayList<>();
        carregarUsuarios();
    }
    private void carregarUsuarios() {
        List<String> linhas = FileManager.lerArquivo(ARQUIVO);
        usuarios.clear();
        
        for (String linha : linhas) {
            Usuario usuario = Usuario.fromFileString(linha);
            if (usuario != null) {
                usuarios.add(usuario);
            }
        }
        if (usuarios.isEmpty()) {
            criarUsuarioPadrao();
        }
    }
    private void salvarUsuarios() {
        List<String> linhas = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            linhas.add(usuario.toFileString());
        }
        FileManager.escreverArquivo(ARQUIVO, linhas);
    }
    private void criarUsuarioPadrao() {
        String senhaAdmin = CriptografiaUtil.criptografar("admin123");
        Usuario admin = new Usuario("admin", senhaAdmin, "Administrador");
        usuarios.add(admin);
        salvarUsuarios();
        System.out.println("Usuário padrão criado: admin / admin123");
    }
    public boolean cadastrarUsuario(String login, String senha, String nome) {
        if (buscarPorLogin(login) != null) {
            return false; // Login já existe
        }
        
        String senhaCriptografada = CriptografiaUtil.criptografar(senha);
        Usuario usuario = new Usuario(login, senhaCriptografada, nome);
        usuarios.add(usuario);
        salvarUsuarios();
        return true;
    }
    public Usuario autenticar(String login, String senha) {
        Usuario usuario = buscarPorLogin(login);
        if (usuario != null && usuario.isAtivo()) {
            if (CriptografiaUtil.verificarSenha(senha, usuario.getSenhaCriptografada())) {
                return usuario;
            }
        }
        return null;
    }
    public boolean atualizarUsuario(Usuario usuario) {
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getId() == usuario.getId()) {
                usuarios.set(i, usuario);
                salvarUsuarios();
                return true;
            }
        }
        return false;
    }
    public boolean alterarSenha(int id, String senhaAtual, String novaSenha) {
        Usuario usuario = buscarPorId(id);
        if (usuario != null) {
            if (CriptografiaUtil.verificarSenha(senhaAtual, usuario.getSenhaCriptografada())) {
                String novaSenhaCriptografada = CriptografiaUtil.criptografar(novaSenha);
                usuario.setSenhaCriptografada(novaSenhaCriptografada);
                return atualizarUsuario(usuario);
            }
        }
        return false;
    }
    public boolean desativarUsuario(int id) {
        Usuario usuario = buscarPorId(id);
        if (usuario != null) {
            usuario.setAtivo(false);
            return atualizarUsuario(usuario);
        }
        return false;
    }
    public Usuario buscarPorId(int id) {
        return usuarios.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElse(null);
    }
    public Usuario buscarPorLogin(String login) {
        return usuarios.stream()
                .filter(u -> u.getLogin().equalsIgnoreCase(login))
                .findFirst()
                .orElse(null);
    }
    public List<Usuario> listarAtivos() {
        List<Usuario> ativos = new ArrayList<>();
        for (Usuario u : usuarios) {
            if (u.isAtivo()) {
                ativos.add(u);
            }
        }
        return ativos;
    }
    public void exibirTodos() {
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
            return;
        }
        
        System.out.println("\n=== USUÁRIOS CADASTRADOS ===");
        for (Usuario usuario : usuarios) {
            System.out.printf("ID: %d | Login: %s | Nome: %s | Status: %s%n", 
                    usuario.getId(), 
                    usuario.getLogin(), 
                    usuario.getNome(),
                    usuario.isAtivo() ? "Ativo" : "Inativo");
        }
    }
}
