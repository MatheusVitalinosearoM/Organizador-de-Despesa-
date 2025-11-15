package utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    
    private static final String DATA_DIR = "data/";
    public static List<String> lerArquivo(String nomeArquivo) {
        List<String> linhas = new ArrayList<>();
        String caminhoCompleto = DATA_DIR + nomeArquivo;
        
        File arquivo = new File(caminhoCompleto);
        if (!arquivo.exists()) {
            return linhas; // Retorna lista vazia se arquivo não existe
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (!linha.trim().isEmpty()) {
                    linhas.add(linha);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo " + nomeArquivo + ": " + e.getMessage());
        }
        
        return linhas;
    }
    public static boolean escreverArquivo(String nomeArquivo, List<String> linhas) {
        String caminhoCompleto = DATA_DIR + nomeArquivo;
    
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoCompleto))) {
            for (String linha : linhas) {
                writer.write(linha);
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("Erro ao escrever arquivo " + nomeArquivo + ": " + e.getMessage());
            return false;
        }
    }
    public static boolean adicionarLinha(String nomeArquivo, String linha) {
        String caminhoCompleto = DATA_DIR + nomeArquivo;
        
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoCompleto, true))) {
            writer.write(linha);
            writer.newLine();
            return true;
        } catch (IOException e) {
            System.err.println("Erro ao adicionar linha ao arquivo " + nomeArquivo + ": " + e.getMessage());
            return false;
        }
    }
    public static boolean arquivoExiste(String nomeArquivo) {
        File arquivo = new File(DATA_DIR + nomeArquivo);
        return arquivo.exists();
    }
    public static boolean deletarArquivo(String nomeArquivo) {
        File arquivo = new File(DATA_DIR + nomeArquivo);
        return arquivo.delete();
    }
    public static void inicializarDiretorio() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
            System.out.println("Diretório de dados criado: " + DATA_DIR);
        }
    }
}
